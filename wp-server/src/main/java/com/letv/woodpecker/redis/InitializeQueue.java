package com.letv.woodpecker.redis;

import com.letv.woodpecker.dao.AppInfoDao;
import com.letv.woodpecker.model.AppInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhusheng on 17/3/16.
 */
@Slf4j
public class InitializeQueue implements InitializingBean, DisposableBean {
    @Autowired
    private AppInfoDao appInfoDao;
    private QueueListener listener;
    private RedisTemplate redisTemplate;
    private static List<RedisQueue> listQ = new ArrayList<>();

    /**
     * 创建启动redis连接并监听
     */
    public void createQueue() {
        List<AppInfo> apps = appInfoDao.queryList(null);
        if (apps != null) {
            for (AppInfo app : apps) {
                RedisQueue queue = new RedisQueue();
                queue.setListener(listener);
                queue.setRedisTemplate(redisTemplate);
                queue.setKey(app.getAppName());
                listQ.add(queue);
                try {
                    queue.start();
                } catch (Exception e) {
                    log.error("启动redis异常!{}", e);
                }
            }
        }
    }

    public QueueListener getListener() {
        return listener;
    }

    public void setListener(QueueListener listener) {
        this.listener = listener;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 销毁redis连接
     *
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        for (RedisQueue queue : listQ) {
            queue.destroy();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //创建队列
        createQueue();
        //启动定时添加队列
        addQueueTimer();
    }

    public void addQueueTimer() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        long delay = 60000;
        long initDelay = 0;
        executor.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        log.info("执行定时任务,检查是否有新应用!");
                        List<AppInfo> apps = appInfoDao.queryList(null);
                        if (null != apps) {
                            for (AppInfo app : apps) {
                                RedisQueue queue = new RedisQueue();
                                queue.setListener(listener);
                                queue.setRedisTemplate(redisTemplate);
                                queue.setKey(app.getAppName());
                                if (!listQ.contains(queue)) {
                                    listQ.add(queue);
                                    try {
                                        log.info("启动新的监听队列,appName={}", app.getAppName());
                                        queue.start();
                                    } catch (Exception e) {
                                        log.error("启动redis异常!{}", e);
                                    }
                                } else {
                                    queue = null;
                                }
                            }
                        }
                    }
                },
                initDelay,
                delay,
                TimeUnit.MILLISECONDS);
    }
}
