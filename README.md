# woodpecker 异常日志收集系统
本系统旨在为分布式应用提供一个日常日志收集功能，根据配置规则实时报警，并将收集的异常日志落地通过图表等方式展现，直观展现系统异常情况，衡量系统KPI。
本系统是一个轻量级的日志收集系统。客户端将应用异常日志发送到redis队列，服务端监听该队列收集日志告警并落地，web端分析展现。
client端以jar包引用，对代码无侵入，开辟少量线程发送消息，几乎无系统资源消耗，并设有保护机制，对应用服务几乎无影响。
项目四个模块如下：
1.wp-client:日志收集客户端
2.wp-data-model:dao层，操作数据库
3.wp-server:服务端，依赖wp-data-model
4.wp-webapp:web端，对异常日志的界面操作，依赖wp-data-model
系统流程及部署：
编译wp-client模块，将jar包引用到应用工程中，应用日志框架需使用log4j或logback,wp-client通过在日志上下文为logger添加一个自定义的appender，让应用
在产生异常的同时向redis队列发送消息，redis参数需配置到应用的pom.xml中，具体参数见wp-client下的redis-single.xml或redis-cluster.xml。server
服务可根据实际情况部署多台，运行wp-server下的run-deploy-server.sh脚本部署。client会按应用分队列，server端监听这些应用队列获取消息，根据mongodb
里的配置规则实时报警。wp-webapp为web应用，启动web应用可进入后台管理系统，在应用管理模块注册应用后，server才可正常接收日志，告警规则配置和异常展现均在
后台管理系统中。server端和web端需配置mongodb，具体参数见wp-data-model模块下mongo-dao.xml（单机）和mongo-cluster.xml（副本集）。
