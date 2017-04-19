package com.letv.woodpecker.service.impl;

import com.letv.woodpecker.dao.AlarmConfigDao;
import com.letv.woodpecker.model.AlarmConfig;
import com.letv.woodpecker.service.AlarmConfigService;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhusheng on 17/3/29.
 */
@Service("alarmConfigService")
public class AlarmConfigServiceImpl implements AlarmConfigService{

    @Resource
    private AlarmConfigDao alarmConfigDao;

    @Override
    public List<AlarmConfig> queryAlarmConfigs(String userId,int pageStart,int pageSize) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        if(pageSize != Integer.MAX_VALUE){
            query.skip(pageStart).limit(pageSize);
        }
        return alarmConfigDao.queryList(query);
    }

    @Override
    public long getConfigsCount(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return alarmConfigDao.getCount(query);
    }

    @Override
    public void saveAlarmConfig(AlarmConfig alarmConfig) {
        alarmConfigDao.save(alarmConfig);
    }

    @Override
    public void deleteConfig(AlarmConfig config) {
        alarmConfigDao.deleteConfig(config);
    }
}
