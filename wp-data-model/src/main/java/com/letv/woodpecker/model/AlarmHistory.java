package com.letv.woodpecker.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhusheng on 17/3/16.
 */
@Data
public class AlarmHistory implements Serializable{
    private String appName;
    private String ip;
    private String exceptionType;
    private String alarmTime;  //报警时间
}
