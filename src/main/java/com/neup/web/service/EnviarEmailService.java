package com.neup.web.service;

import com.neup.web.dto.ContactoDTO;
import com.neup.web.model.Email;
import com.neup.web.utils.ConfigurationReader;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.neup.web.utils.ConstantesEntorno.SPRING_MAIL_USERNAME;

@Service
public class EnviarEmailService {
    private final JavaMailSender mailSender;

    public EnviarEmailService(final JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

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

    public void sendContactEmail(ContactoDTO dto) {
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("templates/plantilla/email_contacto.html")) {

            if (is == null) {
                throw new RuntimeException("No se encontró la plantilla email_contacto.html");
            }

            String html = new String(is.readAllBytes(), StandardCharsets.UTF_8)
                    .replace("[NOMBRE]", dto.getNombre())
                    .replace("[EMAIL]", dto.getEmail())
                    .replace("[ASUNTO]", dto.getAsunto())
                    .replace("[MENSAJE]", dto.getMensaje())
                    .replace("[TELEFONO]", "N/A");

            // Confirmación al usuario que escribió
            sendEmail(Email.builder()
                    .emailPara(dto.getEmail())
                    .asunto("NEUP - Hemos recibido tu mensaje")
                    .cuerpoEmail(html)
                    .build());

            // Notificación interna al administrador
            String adminEmail = ConfigurationReader.getProperty(SPRING_MAIL_USERNAME);
            sendEmail(Email.builder()
                    .emailPara(adminEmail)
                    .asunto("NEUP - Nueva consulta de: " + dto.getNombre() + " | " + dto.getAsunto())
                    .cuerpoEmail(html)
                    .build());

        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la plantilla de contacto", e);
        }
    }
}

