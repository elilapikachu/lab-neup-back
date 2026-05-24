package com.neup.web.service;

import com.neup.web.model.Email;
import com.neup.web.utils.ConfigurationReader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.neup.web.utils.ConstantesEntorno.SPRING_MAIL_USERNAME;

@Service
public class EnviarEmailService {
    private final JavaMailSender mailSender;

    public EnviarEmailService(final JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Service - recibe solo el objeto Email
    public void sendEmail(Email email) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(email.getEmailPara());
        mensaje.setSubject(email.getAsunto());
        mensaje.setText(email.getCuerpoEmail());
        mensaje.setFrom(ConfigurationReader.getProperty(SPRING_MAIL_USERNAME));
        mailSender.send(mensaje);
    }
}
