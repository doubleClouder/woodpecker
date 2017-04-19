package com.letv.woodpecker.redis.impl;

import com.letv.woodpecker.model.ExceptionInfo;
import com.letv.woodpecker.redis.QueueListener;
import com.letv.woodpecker.service.ConsumeServer;
import com.letv.woodpecker.utils.ThreadPoolManageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.Resource;

/**
 * Created by zhusheng on 17/3/14.
 */
@Slf4j
public class RedisQueueListener implements QueueListener<String> {
    //private ExecutorService threadpool = Executors.newFixedThreadPool(10);
    @Autowired
    private ConsumeServer consumeServer;
    @Resource
    private ThreadPoolManageUtil threadPoolManageUtil;

    @Override
    public void onMessage(String value) {
        ExceptionInfo exceptionInfo = consumeServer.parseExceptionInfo(value);
        //threadpool.execute(new MessageRunnable(value));
        if (StringUtils.isNotEmpty(exceptionInfo.getAppName())) {
            threadPoolManageUtil.getThreadPoolByKey(exceptionInfo.getAppName()).execute(new MessageRunnable(exceptionInfo));
        }
    }

    private class MessageRunnable implements Runnable {
        private ExceptionInfo exceptionInfo;
        public MessageRunnable(ExceptionInfo exceptionInfo) {
            this.exceptionInfo = exceptionInfo;
        }
        @Override
        public void run() {
            log.info("处理日志消息!");
            consumeServer.doConsume(exceptionInfo);
        }
    }
}
