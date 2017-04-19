package com.letv.woodpecker.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.letv.woodpecker.dao.AlarmConfigDao;
import com.letv.woodpecker.dao.AlarmHistoryDao;
import com.letv.woodpecker.dao.ExceptionInfoDao;
import com.letv.woodpecker.model.AlarmConfig;
import com.letv.woodpecker.model.AlarmHistory;
import com.letv.woodpecker.model.ExceptionInfo;
import com.letv.woodpecker.service.ConsumeServer;
import com.letv.woodpecker.service.MailSenderService;
import com.letv.woodpecker.utils.MailUtil;
import com.letv.woodpecker.utils.Md5Util;
import com.letv.woodpecker.utils.ThreadPoolManageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhusheng on 17/3/15.
 */
@Service("consumeServer")
public class ConsumeServerImpl implements ConsumeServer {

    Logger logger = LoggerFactory.getLogger("consumeServer");
    private static final String EXCEPTION = "Exception:";
    private static final String ERROR = "Error:";
    private static final String ALL = "all";
    private static final String EACH = "each";
    @Autowired
    private ExceptionInfoDao exceptionInfoDao;
    @Autowired
    private AlarmConfigDao alarmConfigDao;
    @Autowired
    private AlarmHistoryDao alarmHistoryDao;
    @Autowired
    private MailSenderService mailSenderService;
    @Resource
    private ThreadPoolManageUtil threadPoolManageUtil;

    @Override
    public void doConsume(ExceptionInfo exceptionInfo) {
        //获取该条异常所有的报警规则
        List<AlarmConfig> configs = alarmConfigDao.queryList(exceptionInfo.getAppName(),exceptionInfo.getIp(),exceptionInfo.getExceptionType());
        for(AlarmConfig config : configs){
            exceptionAlarm(config,exceptionInfo);
        }
        try{
            exceptionInfoDao.save(exceptionInfo);
        }catch (Exception e){
            logger.error("save exceptionInfo fail! msg {}",exceptionInfo);
        }
    }

    public ExceptionInfo parseExceptionInfo(String exceptionInfo){
        JSONObject jsonObject = JSON.parseObject(exceptionInfo);
        if(jsonObject == null){
            return null;
        }
        ExceptionInfo msgObject = new ExceptionInfo();
        String msg = jsonObject.getString("msg");
        String exceptionName = "";
        if(msg!=null && msg.contains(EXCEPTION)  ){
            exceptionName = msg.substring(0,msg.indexOf(EXCEPTION)+EXCEPTION.length());
            exceptionName = exceptionName.substring(exceptionName.lastIndexOf(".")+1,exceptionName.length()-1);
        }
        if(msg!=null && msg.contains(ERROR)  ){
            exceptionName = msg.substring(0,msg.indexOf(ERROR)+ERROR.length());
            exceptionName = exceptionName.substring(exceptionName.lastIndexOf(".")+1,exceptionName.length()-1);
        }
        String md5Str = msg.substring(msg.indexOf(" - ")+3);
        msgObject.setContentMd5(Md5Util.getMd5(md5Str));
        msgObject.setExceptionType(exceptionName);
        msgObject.setAppName(jsonObject.getString("appName"));
        msgObject.setIp(jsonObject.getString("ip"));
        msgObject.setLogTime(jsonObject.getString("createTime"));
        msgObject.setCreateTime(timeForNow());
        msgObject.setMsg(msg);
        return msgObject;
    }

    private void exceptionAlarm(final AlarmConfig config,final ExceptionInfo exceptionInfo){
        if(config == null){
            return;
        }
        final String appName = config.getAppName();
        final String ip = ALL.equals(config.getIp())? ALL : exceptionInfo.getIp();
        final String exceptionType = ALL.equals(config.getExceptionType())? ALL : exceptionInfo.getExceptionType();
        //获取最近的告警时间
        String latestAlarmTime = alarmHistoryDao.getLatestAlarmTime(appName,ip,exceptionType);
        int current = (int) exceptionInfoDao.getExceptionCount(appName,ip,exceptionType,latestAlarmTime);
        if(current +1 >= config.getThreshold()){
            threadPoolManageUtil.getThreadPoolByKey("mailSend").execute(new Runnable(){
                @Override
                public void run() {
                    doAlarm(appName, ip, exceptionInfo.getIp(), exceptionType, config.getEmail(), exceptionInfo.getMsg());
                }
            });
        }
    }

    public void doAlarm(String appName,String ip,String realIP,String exceptionType,String emailAddr,String msg){
        mailSenderService.sendMail(appName,realIP,emailAddr,msg);
        //写到告警历史表
        AlarmHistory history = new AlarmHistory();
        history.setAppName(appName);
        history.setIp(ip);
        history.setExceptionType(exceptionType);
        history.setAlarmTime(timeForNow());
        alarmHistoryDao.save(history);
    }

    private String timeForNow(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return format.format(now);
    }


}
