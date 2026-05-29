package com.neup.web.service;

import com.neup.web.dto.ContactoDTO;
import com.neup.web.model.Email;
import com.neup.web.utils.ConfigurationReader;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.neup.web.utils.ConstantesEntorno.SPRING_MAIL_USERNAME;

@Service
public class EnviarEmailService {
    private final SendGrid sendGrid;

    public EnviarEmailService(final SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    public void sendEmail(Email emailModel) {
        String fromAddress = ConfigurationReader.getProperty(SPRING_MAIL_USERNAME);
        com.sendgrid.helpers.mail.objects.Email from = new com.sendgrid.helpers.mail.objects.Email(fromAddress);
        com.sendgrid.helpers.mail.objects.Email to = new com.sendgrid.helpers.mail.objects.Email(emailModel.getEmailPara());
        Content content = new Content("text/html", emailModel.getCuerpoEmail());
        Mail mail = new Mail(from, emailModel.getAsunto(), to, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            if (response.getStatusCode() >= 400) {
                throw new RuntimeException("Error al enviar correo: " + response.getStatusCode() + " " + response.getBody());
            }
        } catch (IOException e) {
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

            sendEmail(Email.builder()
                    .emailPara(dto.getEmail())
                    .asunto("NEUP - Hemos recibido tu mensaje")
                    .cuerpoEmail(html)
                    .build());

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
