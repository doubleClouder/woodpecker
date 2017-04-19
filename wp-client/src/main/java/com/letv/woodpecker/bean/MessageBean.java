package com.letv.woodpecker.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhusheng on 17/3/13.
 */
@Data
public class MessageBean implements Serializable{
    private String appName ;
    private String ip;
    private String msg;
    private String createTime;
}
