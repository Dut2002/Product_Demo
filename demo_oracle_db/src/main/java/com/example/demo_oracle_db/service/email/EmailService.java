package com.example.demo_oracle_db.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine engine;

    public void sendSimpleMail(String toEmail, String header, String nameRec, String subject, String content, String link, int option) throws MessagingException {
        Context context = new Context();
        context.setVariable("header", header);
        context.setVariable("name", nameRec);
        context.setVariable("content", content);
        context.setVariable("link", link);

        String body = switch (option) {
            case 1 -> engine.process("SendActive", context);
            case 2 -> engine.process("SendNewProduct", context);
            case 3 -> engine.process("SendBanAcc", context);
            default -> engine.process("SendEmail", context);
        };
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
    }

}
