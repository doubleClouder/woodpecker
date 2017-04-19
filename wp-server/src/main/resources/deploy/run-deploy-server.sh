#!/bin/bash
base="${deploy.base}"
name="${project.name}"
deploy="${deploy.base}"
webapp="${deploy}/${project.name}"
servers=(${deploy.wp.web.server})

serverCount=${#servers[@]}
serial_no=`date +%s`
i=0
while [ $i -lt $serverCount ]
do
    server=${servers[$i]}
    echo "deploy $name on $server"
    host=`echo $server | cut -d: -f1`
    port=`echo $server | cut -d: -f2`

    pids=$(ssh root@$host "ps ux|grep 'Dproject.name=woodpecker'|grep -v grep"|awk '{print $2}')
    if [ "$pids" == "" ];then
            echo "woodpecker is not running. exists"
    else
        for pid in $pids
        do
            ssh root@$host "kill $pid"
            echo "stoped[$pid]"
        done
    fi
    echo "delete $name  $name.tar.gz"
    ssh root@$host "rm -rf $webapp ${deploy}/$name.tar.gz"
    scp artifacts/$name.tar.gz root@$host:$deploy
    echo "unzip server!"
    ssh root@$host "cd ${deploy} && tar -xzvf $name.tar.gz"
    ssh root@$host "chmod 777 ${deploy}/$name/bin/start.sh && chmod 777 ${deploy}/$name/bin/stop.sh"
    i=$((i+1))
done
i=0
while [ $i -lt $serverCount ]
do
    server=${servers[$i]}
    echo "deploy $name on $server"
    host=`echo $server | cut -d: -f1`
    port=`echo $server | cut -d: -f2`
    echo "start server!"
    ssh root@$host "${deploy}/$name/bin/start.sh $port"
    sleep 10
    i=$((i+1))
done
echo "all done."
