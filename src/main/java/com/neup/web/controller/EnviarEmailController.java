package com.neup.web.controller;

import com.neup.web.service.EnviarEmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Email", description = "Enviar emails")
@RequestMapping("/api/email")
@CrossOrigin(origins = "*")
public class EnviarEmailController {
    private final EnviarEmailService emailService;

    public EnviarEmailController(final EnviarEmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")  // Define una ruta POST para enviar correos
    public String enviarCorreo(@RequestParam String to,
                               @RequestParam String subject,
                               @RequestParam String body) {
        emailService.sendEmail(to, subject, body);
        return "Correo enviado correctamente a " + to;
    }
}
