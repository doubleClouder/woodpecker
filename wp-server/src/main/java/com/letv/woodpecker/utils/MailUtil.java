package com.letv.woodpecker.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by zhusheng on 17/3/16.
 */
public class MailUtil {

    private Logger logger = LoggerFactory.getLogger("consumeServer");
    private static Properties properties;
    //发件邮箱
    @Value("${mail.user}")
    private static String sendFrom;
    @Value("${mail.pass}")
    private static String password;
    static{
        properties = new Properties();
        // 发送服务器需要身份验证
        //properties.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        properties.setProperty("mail.host", "smtp.163.com");
        // 发送邮件协议名称
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        sendFrom = "le_monitor@163.com";
        password = "123456789le1";

    }
    public static void sendMail(String appName,String ip,String emails,String content)  {
        // 设置环境信息
        Session session = Session.getInstance(properties);
        // 创建邮件对象
        Message msg = new MimeMessage(session);
        try {
            msg.setSubject(appName+"系统异常报警");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        // 设置邮件内容
        try {
            msg.setText("亲~,ip="+ip+",异常信息如下:\n"+content);
            // 设置发件人
            msg.setFrom(new InternetAddress(sendFrom));
            Transport transport = session.getTransport();
            // 连接邮件服务器
            transport.connect(properties.getProperty("mail.host"),sendFrom,password);
            // 发送邮件,多个邮箱用';'分割
            String [] addrs = emails.split(";");
            Address[] addresses = new Address[addrs.length];
            for(int i=0; i<addrs.length; i++){
                addresses[i] = new InternetAddress(addrs[i]);
            }
            transport.sendMessage(msg, addresses);
            // 关闭连接
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        ApplicationContext context = new FileSystemXmlApplicationContext("classpath:ApplicationContext.xml");
        MailUtil.sendMail("openeco","127.0.0.1","zhusheng@le.com","1213214355");
    }

}
