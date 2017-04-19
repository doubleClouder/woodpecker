#!/bin/bash
pids=`ps ux|grep 'Dproject.name=woodpecker'|grep -v grep|awk '{print $2}'`

if [ "$pids" == "" ];then
        echo "woodpecker is not running. exists"
else
	for pid in $pids
	do
		kill $pid
		echo "stoped[$pid]"
	done
fi
