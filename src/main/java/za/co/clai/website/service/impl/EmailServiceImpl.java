package za.co.clai.website.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import za.co.clai.website.model.EmailModel;
import za.co.clai.website.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${email.send.from}")
    private String emailFrom;

    @Value("${email.send.to}")
    private String emailTo;
    private final JavaMailSender emailSender;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Async("threadPoolTaskExecutor")
    public void sendSimpleMessage(EmailModel emailModel) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(emailTo);
        message.setReplyTo(emailModel.getEmail());
        message.setSubject("CLAI Website Enquiry." );
        message.setText(emailModel.getComment());
        emailSender.send(message);
    }

}
