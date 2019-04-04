#!/bin/bash
export LANG="en_US.UTF-8"  
#export PS4='[+] $(date +%H:%M:%S) info (@$LINENO): '

## 分支名
tag_name=$1
IN_TAG=$tag_name
## git地址
GIT_DIR='git@github.com:564907422/paas-service-web.git'
## 项目名
GIT_PROJ_NAME=$(echo $GIT_DIR|awk -F/ '$0=$NF'|awk -F\. '$0=$1')
## 工作目录
WORKSPACE='/home/darren/data'
## 发版用户
BUILD_USER='xiaozq'
## 线上服务器地址
HOST='192.144.172.239'
## 服务器部署目录
DEPLOY_DIR='/data/xiaoyaospace'
## 使用端口号
SERVER_PORT=8085
## 检测项目启动地址
HEALTH_CHECK_FILE='check'
echo $build_type

#切换到工作目录
cd $WORKSPACE
test ! -d "$WORKSPACE/$GIT_PROJ_NAME" && echo "git clone $GIT_DIR" && git clone $GIT_DIR

#进入项目目录
pushd $GIT_PROJ_NAME >/dev/null

echo $IN_TAG|egrep -q '^d-|^b-|^r-'

if [ $? -ne 0 ];then

	git fetch origin
	if [ $? -ne 0 ];then
                echo "无法连接gitlab！"
                exit 1;
        fi

	git rev-list origin/$IN_TAG >/dev/null

	if [ $? -ne 0 ];then
		echo "$tag_name 不存在！"
		exit 1;
	fi

	echo '检查代码是否合并...'

	diff=$(git log origin/master ^origin/$IN_TAG| wc -l)
	if [ $diff -ne 0 ];then
		echo "请先合并master代码到分支$IN_TAG!"
		exit 1;
	fi

	echo '代码已经合并! '

	git pull
	git fetch --tags

	git checkout $IN_TAG
	if [ $? -ne 0 ];then
		exit 1;
	fi

	git pull

	git checkout master
	if [ $? -ne 0 ];then
                exit 1;
        fi
	git reset --hard origin/master
	if [ $? -ne 0 ];then
                exit 1;
        fi
	git status

	git merge $IN_TAG
	if [ $? -ne 0 ];then
		echo "合并到master失败！"
		exit 1
	fi
	git status

	timestamp=$(date +%y%m%d%H%M)
	IN_TAG=r-$timestamp-$BUILD_USER
        git fetch --tags
        num=$(git tag -l $IN_TAG|wc -l)
        if [ $num -eq 1 ];then
                echo "Tag冲突，一分钟后重新打Tag"
                sleep 60
                timestamp=$(date +%y%m%d%H%M)
                IN_TAG=r-$timestamp-$BUILD_USER
        fi
	git tag -a -m "$tag_name" $IN_TAG master

	git push origin master
	if [ $? -ne 0 ];then
		echo "推送代码到origin失败"
		exit 1
	fi

	git push origin --tags
	if [ $? -ne 0 ];then
		echo "推送rtag到origin失败"
		exit 1
	fi
fi

git fetch --tags
git reset --hard $IN_TAG
if [ $? -ne 0 ];then
	echo "$IN_TAG 不存在！"
	exit 1
fi

merge_count=$(find . -regex '.*\.js\|.*\.html\|.*\.htm\|.*\.css\|.*\.xml\|.*\.jsp\|.*\.properties' |xargs grep "<<<<<<< HEAD" 2>/dev/null |wc -l)
if [ $merge_count -ne 0 ]; then
	find . -regex '.*\.js\|.*\.html\|.*\.htm\|.*\.css\|.*\.xml\|.*\.jsp\|.*\.properties' |xargs grep "<<<<<<< HEAD" 2>/dev/null
	echo "请解决代码冲突后再发布！"
	exit 1
fi

echo "mvn clean package -DskipTests=true"
mvn clean package -DskipTests=true
if [ $? -ne 0 ];then
	exit 1
fi

popd

JAR_NAME=$(ls -al "$WORKSPACE/$GIT_PROJ_NAME/target" | grep "jar$" | awk '{print $9}')
echo "$JAR_NAME"

ssh root@$HOST "test -d $DEPLOY_DIR/$GIT_PROJ_NAME || mkdir -p $DEPLOY_DIR/$GIT_PROJ_NAME"

if [ $? -ne 0 ];then
    echo "远程执行命令失败！"
    exit 1
    #continue
fi


echo "scp -p \"$WORKSPACE/$GIT_PROJ_NAME/target/$JAR_NAME\"  root@$HOST:/tmp/ "
scp -p "$WORKSPACE/$GIT_PROJ_NAME/target/$JAR_NAME"  root@$HOST:/tmp/

if [ $? -ne 0 ];then
     echo "同步文件失败！"
     exit 1
     #continue
fi


ssh root@$HOST "rm -f $DEPLOY_DIR/$GIT_PROJ_NAME/$JAR_NAME && cp -rf /tmp/$JAR_NAME $DEPLOY_DIR/$GIT_PROJ_NAME/"

ssh root@$HOST "sh $DEPLOY_DIR/$GIT_PROJ_NAME/start.sh $JAR_NAME $SERVER_PORT $HEALTH_CHECK_FILE $DEPLOY_DIR"


ssh root@$HOST "echo $IN_TAG > $DEPLOY_DIR/$GIT_PROJ_NAME/git_version"

echo "发布成功 $IN_TAG"