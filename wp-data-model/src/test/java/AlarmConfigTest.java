import com.letv.woodpecker.dao.AlarmConfigDao;
import com.letv.woodpecker.dao.AppInfoDao;
import com.letv.woodpecker.dao.ExceptionInfoDao;
import com.letv.woodpecker.model.AlarmConfig;
import com.letv.woodpecker.model.AppInfo;
import com.letv.woodpecker.model.ExceptionInfo;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by zhusheng on 17/3/20.
// */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( locations = {"classpath:/mongo-dao.xml"})
public class AlarmConfigTest  {
    @Autowired
    private AlarmConfigDao alarmConfigDao;
    @Autowired
    private AppInfoDao appInfoDao;
    @Autowired
    private ExceptionInfoDao exceptionInfoDao;

    @Test
    @Ignore
    public void saveConfig(){
        AlarmConfig alarmConfig = new AlarmConfig();
        alarmConfig.setIp("each");
        alarmConfig.setAppName("openeco");
        alarmConfig.setExceptionType("each");
        alarmConfig.setEmail("zhusheng@le.com");
        alarmConfig.setThreshold(1);
        alarmConfigDao.save(alarmConfig);

    }

    @Test
    @Ignore
    public void addApp(){
        AppInfo appInfo = new AppInfo();
        appInfo.setUserId("xxx");
        appInfo.setAppName("lms");
        appInfo.setIp("127.0.0.1");
        appInfoDao.save(appInfo);
    }

    @Test
    @Ignore
    public void groupTest(){
        String[] fields = new String[]{"appName","exceptionType"};
        BasicDBList dbList = exceptionInfoDao.mongoGroup(null,null,fields);
        for(int i=0; i<dbList.size(); i++){
            DBObject dbObject = (DBObject) dbList.get(i);

        }
    }
}
