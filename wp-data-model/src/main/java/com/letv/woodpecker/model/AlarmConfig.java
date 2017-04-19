package com.letv.woodpecker.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhusheng on 17/3/16.
 */
@Data
public class AlarmConfig implements Serializable{
    private String userId;
    private String appName;
    private String ip;
    private String exceptionType;
    private int threshold;
    private String email;
}
