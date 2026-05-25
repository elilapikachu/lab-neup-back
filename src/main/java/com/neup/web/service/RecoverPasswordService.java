package com.neup.web.service;

import com.neup.web.model.Email;
import com.neup.web.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static com.neup.web.utils.ConstantesGenerales.CARACTERES;
import static com.neup.web.utils.ConstantesNumericas.CERO;
import static com.neup.web.utils.ConstantesNumericas.DOCE;
import static com.neup.web.utils.repository.ConstantesPersonaRepository.*;

@Service
@RequiredArgsConstructor
public class RecoverPasswordService {
    private final UsuarioRepository usuarioRepository;
    private final EnviarEmailService emailService;
    private final TemplateEngine templateEngine;
    private final PasswordEncoder passwordEncoder;

    public void recuperarPassword(String nombreUsuario, String emailUser) {
        String emailDestino;
        String userDestino = nombreUsuario;

        if (emailUser.isEmpty()) {
            Document usuarioDoc = usuarioRepository.findByUsuario(nombreUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado " + nombreUsuario));

            emailDestino = usuarioDoc.getString(EMAIL);

            if (emailDestino == null || emailDestino.isBlank()) {
                throw new RuntimeException("El usuario no tiene email registrado");
            }
        } else {
            Document emailDoc = usuarioRepository.findByEmail(emailUser)
                    .orElseThrow(() -> new RuntimeException("Email no encontrado " + emailUser));

            userDestino = emailDoc.getString(USUARIO);
            emailDestino = emailUser;
        }

        // Generar contraseña temporal
        String passwordTemporal = generarPasswordTemporal();

        // Encriptar con BCrypt
        String passwordEncriptada = passwordEncoder.encode(passwordTemporal);


        boolean actualizado = usuarioRepository.actualizarPasswordTemporalPorUsuario(userDestino, passwordEncriptada, true);

        if (!actualizado) {
            throw new RuntimeException("Error al actualizar la contraseña");
        }

        String cuerpoHtml = generarPlantillaEmail(userDestino, passwordTemporal, emailDestino);

        Email email = Email.builder()
                .emailPara(emailDestino)
                .asunto("Recuperación de Contraseña - NEUP")
                .cuerpoEmail(cuerpoHtml)
                .build();

        emailService.sendEmail(email);
    }

    private String generarPasswordTemporal() {
        StringBuilder password = new StringBuilder();
        for (int i = CERO; i < DOCE; i++) {
            int indice = (int) (Math.random() * CARACTERES.length());
            password.append(CARACTERES.charAt(indice));
        }

        return password.toString();
    }

    private String generarPlantillaEmail(String userDestino, String password, String emailDestino) {
        Context context = new Context();
        context.setVariable("usuario", userDestino);
        context.setVariable("contrasena", password);
        context.setVariable("email", emailDestino);

        return templateEngine.process("plantilla/email_recuperacion_contrasena.html", context);
    }
}
