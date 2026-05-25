package com.neup.web.service;

import com.neup.web.model.Email;
import com.neup.web.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class RecoverPasswordService {
    private final UsuarioRepository usuarioRepository;
    private final EnviarEmailService emailService;
    private final TemplateEngine templateEngine;

    public void recuperarPassword(String nombreUsuario, String emailUser) {
        String emailDestino;
        String password;
        String userDestino = nombreUsuario;

        if (emailUser.isEmpty()) {
            Document usuarioDoc = usuarioRepository.findByUsuario(nombreUsuario)
                    .orElseThrow(() -> new RuntimeException(STR."Usuario no encontrado: \{nombreUsuario}"));

            emailDestino = usuarioDoc.getString("email");
            password = usuarioDoc.getString("password");

            if (emailDestino == null || emailDestino.isBlank()) {
                throw new RuntimeException("El usuario no tiene email registrado");
            }
        } else {
            Document emailDoc = usuarioRepository.findByEmail(emailUser)
                    .orElseThrow(() -> new RuntimeException(STR."Email no encontrado \{emailUser}"));

            userDestino = emailDoc.getString("usuario");
            password = emailDoc.getString("password");
            emailDestino = emailUser;
        }

        // Crear contexto para Thymeleaf con las variables
        Context context = new Context();
        context.setVariable("usuario", userDestino);
        context.setVariable("contrasena", password);
        context.setVariable("email", emailDestino);

        // Procesar plantilla HTML
        String cuerpoHtml = templateEngine.process("plantilla/email_recuperacion_contrasena", context);

        // Crear y enviar email
        Email email = Email.builder()
                .emailPara(emailDestino)
                .asunto("Recuperación de Contraseña - NEUP")
                .cuerpoEmail(cuerpoHtml)
                .build();

        emailService.sendEmail(email);
    }
}
