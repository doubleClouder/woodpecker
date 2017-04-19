#! /bin/sh
exec ${deploy.java.home}/bin/java -jar ${deploy.resin.home}/lib/resin.jar $* --log-directory /letv/apps/resin/log/${project.name} -conf \
