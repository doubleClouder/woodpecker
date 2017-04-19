package com.letv.woodpecker.service;

import com.letv.woodpecker.model.AlarmConfig;

import java.util.List;

/**
 * Created by zhusheng on 17/3/29.
 */
public interface AlarmConfigService {

    List<AlarmConfig> queryAlarmConfigs(String userId,int pageStart,int pageSize);
    long getConfigsCount(String userId);
    void saveAlarmConfig(AlarmConfig alarmConfig);
    void deleteConfig(AlarmConfig config);
}
