#!/bin/sh

echo "接收到的参数 $#"

PROJ_NAME=$1
SERVER_PORT=$2
HEALTH_CHECK_FILE=$3
DEPLOY_DIR=$4
HEALTCHECKURL="http://127.0.0.1:$SERVER_PORT/$HEALTH_CHECK_FILE"

#ps -ef | grep "$PROJ_NAME" |grep -v grep
ps -ef | grep "$PROJ_NAME" |grep -v grep|grep -v 'start.sh' |awk 'NR==1{print $2}'|xargs kill -9 >/dev/null 2>&1

java -server -Xmx256m -jar -Dspring.profiles.active=prod "$DEPLOY_DIR/$PROJ_NAME" >/tmp/paas.log &

flag=0
for i in $(seq 30);do
	sleep 1
	httpcode=$(curl -o /dev/null -s -m 1 --connect-timeout 1 -w %{http_code}  "$HEALTCHECKURL")
	echo "Http check $HEALTCHECKURL httpcode is $httpcode"
	if [ "X$httpcode" == "X200" ];then
		echo -e "\nStart $PROJ_NAME is OK .\n"
                flag=1
		break
	fi
done
if [ $flag -eq 0 ];then
	echo -e "\nStart $PROJ_NAME is FAIL .\n"
	exit 1
fi
