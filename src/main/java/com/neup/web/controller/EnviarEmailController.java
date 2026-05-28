package com.neup.web.controller;

import com.neup.web.dto.ContactoDTO;
import com.neup.web.dto.EnviarEmailDTO;
import com.neup.web.model.Email;
import com.neup.web.service.EnviarEmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Email", description = "Enviar emails")
@RequestMapping("/api/email")
public class EnviarEmailController {
    private final EnviarEmailService emailService;

    public EnviarEmailController(final EnviarEmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> enviarCorreo(@RequestBody EnviarEmailDTO dto) {
        try {
            Email email = Email.builder()
                    .emailPara(dto.getTo())
                    .asunto(dto.getSubject())
                    .cuerpoEmail(dto.getBody())
                    .build();

            emailService.sendEmail(email);
            return ResponseEntity.ok("Correo enviado a " + email.getEmailPara());

        } catch (MailException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar: " + e.getMessage());
        }
    }

    @PostMapping("/contacto")
    public ResponseEntity<String> enviarCorreoContacto(@Valid @RequestBody ContactoDTO dto) {
        try {
            emailService.sendContactEmail(dto);
            return ResponseEntity.ok("Mensaje de contacto enviado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar: " + e.getMessage());
        }
    }
}
