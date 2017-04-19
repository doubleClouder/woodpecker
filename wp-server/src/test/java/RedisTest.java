import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by zhusheng on 17/3/14.
 */
public class RedisTest {

    public static void main(String[] args){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:ApplicationContext.xml");
        //MailUtil.sendMail(null,null,"zhusheng@le.com","1213214355");

    }
}
