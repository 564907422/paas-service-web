#!/bin/bash
export LANG="en_US.UTF-8"  
#export PS4='[+] $(date +%H:%M:%S) info (@$LINENO): '

tag_name='dev'
IN_TAG=$tag_name
GIT_DIR='git@github.com:564907422/paas-service-web.git'
GIT_PROJ_NAME=$(echo $GIT_DIR|awk -F/ '$0=$NF'|awk -F\. '$0=$1')
WORKSPACE='/home/darren/data'
BUILD_USER='xiaozq'
HOST='192.144.172.239'
DEPLOY_DIR='/data/xiaoyaospace'

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

	timestamp=$(date +%y%m%d%H%M)
	IN_TAG=b-$timestamp-$BUILD_USER
        git fetch --tags
        num=$(git tag -l $IN_TAG|wc -l)
        if [ $num -eq 1 ];then
                echo "Tag冲突，一分钟后重新打Tag"
                sleep 60
                timestamp=$(date +%y%m%d%H%M)
                IN_TAG=b-$timestamp-$BUILD_USER
        fi
	git fetch origin
#	git checkout $tag_name 
#	if [ $? -ne 0 ];then
#		exit 1;
#	fi
#	git pull origin $tag_name 
#	if [ $? -ne 0 ];then
#		exit 1;
#	fi

	git tag -a -m "$tag_name" $IN_TAG origin/$tag_name
	git push origin --tags
	if [ $? -ne 0 ];then
		echo "无push权限！"
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

ssh root@$HOST "sh $DEPLOY_DIR/$GIT_PROJ_NAME/start.sh"



