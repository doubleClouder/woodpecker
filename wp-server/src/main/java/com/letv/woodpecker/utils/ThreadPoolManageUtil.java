package com.letv.woodpecker.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.concurrent.*;

/**
 * 线程池管理
 * Created by zhusheng on 17/3/21.
 */
@Slf4j
public class ThreadPoolManageUtil {
    private static ConcurrentMap<String, ThreadPoolExecutor> threadPools = new ConcurrentHashMap<String, ThreadPoolExecutor>();
    private static final int DEFAULT_CORE_SIZE = 4;
    private static final int DEFAULT_MAX_SIZE = 20;
    private static final int DEFAULT_QUEUE_SIZE = 500;

    public ThreadPoolExecutor getThreadPoolByKey(String key) {
        if (!threadPools.containsKey(key)) {
            threadPools.put(key, createThreadPoolExecutor(DEFAULT_CORE_SIZE, DEFAULT_MAX_SIZE, DEFAULT_QUEUE_SIZE, null, key));
        }
        return threadPools.get(key);
    }
    protected static ThreadPoolExecutor createThreadPoolExecutor(int corePoolSize, int maxPoolSize, int queueSize, BlockingQueue<Runnable> copyFromQueue, final String threadName) {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(queueSize);

        if (CollectionUtils.isNotEmpty(copyFromQueue)) {
            copyFromQueue.drainTo(queue, queueSize);
        }

        return new MessageThreadPoolExecutor(threadName, corePoolSize, maxPoolSize, 0L, TimeUnit.MILLISECONDS,
                queue, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.setName(threadName);
                return t;
            }
        }, new MessageRejectedExecutionHandler());
    }

    //多线程执行
    private static class MessageThreadPoolExecutor extends ThreadPoolExecutor {
        @Getter
        private String name;

        public MessageThreadPoolExecutor(String name, int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                         TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                         ThreadFactory threadFactory,
                                         RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
            this.name = name;
        }
    }

    private static class MessageRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            // 监控
            if (log.isInfoEnabled()) {
                MessageThreadPoolExecutor e = (MessageThreadPoolExecutor) executor;

                log.info("do log message, rejectedExecution, ThreadPoolExecutor:{}", e != null ? e.getName() : executor.toString());
            }
        }
    }
}
