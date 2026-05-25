package com.neup.web.service;

import com.neup.web.model.Email;
import com.neup.web.utils.ConfigurationReader;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(email.getEmailPara());
            helper.setSubject(email.getAsunto());
            helper.setText(email.getCuerpoEmail(), true);
            helper.setFrom(ConfigurationReader.getProperty(SPRING_MAIL_USERNAME));

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar correo electrónico", e);
        }
    }
}

