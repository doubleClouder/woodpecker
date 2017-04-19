package com.letv.woodpecker.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

/**
 * Created by zhusheng on 17/4/1.
 */
@Slf4j
@Service
public class MailSenderService {
    @Resource
    private JavaMailSenderImpl mailSender;

    private static String ALARM_SUBJECT = "系统异常报警";
    /**
     * 创建MimeMessage
     * @param mailBean
     * @return
     */
    public MimeMessage createMimeMessage(MailBean mailBean) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(mailBean.getFromName());
            messageHelper.setSubject(mailBean.getSubject());
            messageHelper.setTo(mailBean.getToEmails());
            messageHelper.setText(mailBean.getContext());
        } catch (MessagingException e) {
            log.error("sendMail fail,{}",e);
        } catch (Exception e) {
            log.error("sendMail fail,{}",e);
        }
        return mimeMessage;
    }

    public void sendMail(String appName,String realIP,String emailAddr,String msg) {
        MailBean mailBean = new MailBean();
        String[] addresses = emailAddr.split(";");
        mailBean.setSubject(appName + ALARM_SUBJECT);
        mailBean.setContext("亲~,ip="+realIP+",异常信息如下:\n"+msg);
        mailBean.setToEmails(addresses);
        MimeMessage message = createMimeMessage(mailBean);
        try{
            mailSender.send(message);
        }catch (Exception e){
            log.info("发送邮件异常!e={}",e);
        }
    }

    @Data
    private class MailBean{
        private String from;
        private String fromName;
        private String[] toEmails;
        private String subject;
        private String context;

        public MailBean(){
            this.from = mailSender.getHost();
            this.fromName = mailSender.getUsername();
        }
    }



}
