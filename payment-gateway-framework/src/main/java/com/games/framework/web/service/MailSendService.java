package com.games.framework.web.service;

import com.games.framework.config.MailProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.Properties;

@Slf4j
@Component
public class MailSendService {

    @Resource
    private MailProperties mailProperties;

    /**
     * 发送Gmail邮件
     * @param to 接收者邮箱
     * @param subject 邮件主题
     * @param text 邮件内容
     * @return 发送是否成功
     */
    public boolean sendGmailSimple(String to, String subject, String text) {
        try {
            JavaMailSender mailSender = createGmailSender(mailProperties);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailProperties.getFrom());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            log.error("EmailService 发送 Gmail 失败", e);
        }
        return false;
    }


    public static JavaMailSender createGmailSender(MailProperties mailProperties) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getFrom());
        mailSender.setPassword(mailProperties.getPass());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.connectiontimeout", "5000");
//        props.put("mail.smtp.timeout", "5000");
//        props.put("mail.smtp.writetimeout", "5000");
//        props.put("mail.debug", "true");
        return mailSender;
    }

}
