package com.letv.woodpecker.service;

import com.letv.woodpecker.model.ExceptionInfo;

/**
 * Created by zhusheng on 17/3/15.
 */
public interface ConsumeServer {

    void doConsume(ExceptionInfo exceptionInfo);
    ExceptionInfo parseExceptionInfo(String exceptionInfo);
}
