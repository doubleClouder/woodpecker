#!/bin/bash
base="/letv/webapps"
name="wp-web"
java_home="/letv/apps/jdk"
bin_home="/letv/bin"
conf_home="/letv/conf/resin"
deploy="/letv/webapps"
webapp="${deploy}/wp-web"
servers=(10.130.211.76:8066)
resin="/letv/apps/resin"

serverCount=${#servers[@]}
serial_no=`date +%s`
i=0
while [ $i -lt $serverCount ]
do
    server=${servers[$i]}
    echo "deploy $name on $server"
    host=`echo $server | cut -d: -f1`
    port=`echo $server | cut -d: -f2`

    log_home="/letv/logs/resin/wp-web-$port"
    shell="$bin_home/resin-$name-$port.sh"
    conf="$conf_home/resin-$name-$port.xml"

    ssh root@$host "mkdir -p $bin_home"
    scp *.sh root@$host:$bin_home
    ssh root@$host "rm -f $bin_home/run-deploy*.sh && chmod +x $bin_home/*.sh"
    ssh root@$host "mv -f $bin_home/resin-$name.sh $shell"
    ssh root@$host "chown resin $shell"
    ssh resin@$host "echo $conf >> $shell"

    ssh root@$host "mkdir -p $conf_home"
    ssh root@$host "chown -R resin $conf_home"
    scp resin-$name.xml resin@$host:$conf
    ssh resin@$host "sed -i 's/##port##/$port/' $conf"
    ssh resin@$host "sed -i 's/##host##/$host/' $conf"

    ssh root@$host "mkdir -p $log_home/bak"
    ssh root@$host "chown -R resin $log_home"

    ssh root@$host "$shell stop" || echo "$shell is not running"

    if [ ! -s $deploy/$name-$serial_no.war ] ; then
        ssh root@$host "mkdir -p $webapp"
        ssh root@$host "chown -R resin $deploy"

        scp artifacts/$name.war resin@$host:$deploy
        ssh resin@$host "cd $webapp && rm -rf * && $java_home/bin/jar xf $deploy/$name.war"

        ssh root@$host "mkdir -p $deploy/bak"
        ssh root@$host "cp $deploy/$name.war $deploy/$name-$serial_no.war &&  mv $deploy/$name-$serial_no.war $deploy/bak/"
    fi

    while [ `ssh resin@$host "ps -ef | grep $conf | grep -v grep | wc -l"` -gt 0 ]
    do
	    echo "waiting for $shell resin stop"
    	sleep 1
    done
    
    if [ ! -s $resin/log/watchdog-manager.log ] ; then
    
    	ssh root@$host "mkdir -p $resin/log && cd $resin/log && touch watchdog-manager.log && chown -R resin:root $resin/"
    	
    fi

    ssh resin@$host "$shell start"
    sleep 10
    i=$((i+1))

done
echo "all done."
