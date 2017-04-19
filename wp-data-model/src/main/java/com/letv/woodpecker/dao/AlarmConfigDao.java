package com.letv.woodpecker.dao;

import com.letv.woodpecker.model.AlarmConfig;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhusheng on 17/3/16.
 */
@Repository
public class AlarmConfigDao extends MongoDao<AlarmConfig> {
    @Override
    public Class getEntityClass() {
        return AlarmConfig.class;
    }

    public List<AlarmConfig> queryList(String appName,String ip,String exceptionType){
        Query query = new Query();
        query.addCriteria(Criteria.where("appName").is(appName));
        query.addCriteria(Criteria.where("ip").in(ip,"all","each"));
        query.addCriteria(Criteria.where("exceptionType").in(exceptionType,"all","each"));
        return queryList(query);
    }

    public void deleteConfig(AlarmConfig config){
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(config.getUserId()));
        query.addCriteria(Criteria.where("appName").is(config.getAppName()));
        query.addCriteria(Criteria.where("ip").is(config.getIp()));
        query.addCriteria(Criteria.where("exceptionType").is(config.getExceptionType()));
        delete(query);
    }
}
