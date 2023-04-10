package com.training.erp.util;

import com.tms.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${systemEmail}")
    private String systemEmail;
    @Value("${systemName}")
    private String systemName;

    public void sendVerificationEmail(User user, String code)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = systemEmail;
        String senderName = systemName;
        String subject = "Please complete your registration";
        String content = "Dear [[name]],<br>"
                + "Please copy the code and submit it to complete your registration:<br>"
                + "<h3>Code: [[code]]</h3>"
                + "Thank you,<br>"
                + systemName;
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getUsername());
        content = content.replace("[[code]]", code);
        helper.setText(content, true);
        mailSender.send(message);
    }

    public void sendUserCredential(String email,String firstName,String lastName, String username, String password)
            throws MessagingException, UnsupportedEncodingException {

        String fromAddress = systemEmail;
        String senderName = systemName;
        String subject = "Account Credentials";
        String content = "Dear [[firstName]] [[lastName]],<br>"
                + "Please use the below username and password to login.<br><br><br>"
                + "<h3>Username: [[username]]</h3>"
                + "<h3>Password: [[password]]</h3>"
                + "Thank you,<br>"
                + systemName;
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(email);
        helper.setSubject(subject);
        content = content.replace("[[firstName]]", firstName);
        content = content.replace("[[lastName]]", lastName);
        content = content.replace("[[username]]", username);
        content = content.replace("[[password]]", password);
        helper.setText(content, true);
        mailSender.send(message);
    }






}
