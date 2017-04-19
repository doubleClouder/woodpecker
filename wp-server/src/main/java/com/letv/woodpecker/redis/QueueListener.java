package com.letv.woodpecker.redis;

/**
 * Created by zhusheng on 17/3/14.
 */
public interface QueueListener<T> {
    public void onMessage(T message);
}
