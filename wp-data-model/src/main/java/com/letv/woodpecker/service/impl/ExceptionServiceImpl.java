package com.letv.woodpecker.service.impl;

import com.letv.woodpecker.dao.AppInfoDao;
import com.letv.woodpecker.dao.ExceptionInfoDao;
import com.letv.woodpecker.model.AppInfo;
import com.letv.woodpecker.model.ExceptionInfo;
import com.letv.woodpecker.service.ExceptionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhusheng on 17/3/22.
 */
@Service("exceptionService")
public class ExceptionServiceImpl implements ExceptionService {
    @Resource
    private ExceptionInfoDao exceptionInfoDao;
    @Resource
    private AppInfoDao appInfoDao;

    @Override
    public List<ExceptionInfo> queryAllExceptions(String userId, String appName,String startTime,String endTime, int pageStart, int pageSize) {
        List<AppInfo> apps = appInfoDao.queryAllAppInUser(userId);
        List<String> appNames = new ArrayList<>();
        for(AppInfo app : apps){
            appNames.add(app.getAppName());
        }
        return exceptionInfoDao.queryAllExceptions(appNames,appName,startTime,endTime,pageStart,pageSize);
    }

    @Override
    public long getAllExceptionCount(String userId, String appName, String startTime, String endTime) {
        List<AppInfo> apps = appInfoDao.queryAllAppInUser(userId);
        List<String> appNames = new ArrayList<>();
        for(AppInfo app : apps){
            appNames.add(app.getAppName());
        }
        return exceptionInfoDao.getAllExceptionCount(appNames,appName,startTime,endTime);
    }

    @Override
    public List<ExceptionInfo> queryList(String appName, String exceptionType,String contentMd5, String startTime, String endTime, int pageStart, int pageSize) {
        return exceptionInfoDao.queryPageByDetail(appName,exceptionType,contentMd5,startTime,endTime,pageStart,pageSize);
    }

    @Override
    public List queryListByScheduler(String userId,String appName, String startTime, String endTime) {
        List<AppInfo> apps = appInfoDao.queryAllAppInUser(userId);
        List<String> appNames = new ArrayList<>();
        for(AppInfo app : apps){
            appNames.add(app.getAppName());
        }
        return exceptionInfoDao.queryListByScheduler(appNames,appName,startTime,endTime);
    }

    @Override
    public List classifyByMd5(String appName, String startTime, String endTime, String exceptionType) {
        return exceptionInfoDao.classifyByMd5(appName,startTime,endTime,exceptionType);
    }

    @Override
    public long getCountByDetail(String appName, String exceptionType,String contentMd5, String startTime, String endTime) {
        return exceptionInfoDao.getCountByDetail(appName,exceptionType,contentMd5,startTime,endTime);
    }

    @Override
    public List findExceptionNumByApp(String userId, String startTime, String endTime) {
        List<AppInfo> apps = appInfoDao.queryAllAppInUser(userId);
        List<String> appNames = new ArrayList<>();
        for(AppInfo app : apps){
            appNames.add(app.getAppName());
        }
        return exceptionInfoDao.findExceptionNumByApp(appNames,startTime,endTime);
    }
}
