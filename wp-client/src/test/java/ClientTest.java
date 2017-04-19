//import com.letv.woodpecker.log.impl.LogbackFacility;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Properties;

/**
 * Created by zhusheng on 17/3/20.
 */

public class ClientTest {
    static Logger log = Logger.getLogger(ClientTest.class);
    //private static final Logger log = LoggerFactory.getLogger(ClientTest.class);
    public static void main(String args[]){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/woodpecker.xml");
        //LogbackFacility logbackFacility = (LogbackFacility)context.getBean("logbackFacility");

        try{
            //Thread.sleep(3000);
            String s = null;
            s.equals("");
        }catch (Exception e){
            for(int i=0;i<20;i++){
                try{
                    //Thread.sleep(1000);
                    log.error("clientTest,{}",e);

                }catch (Exception e1){
                    log.info("",e1);
                }
            }
        }
    }


}
