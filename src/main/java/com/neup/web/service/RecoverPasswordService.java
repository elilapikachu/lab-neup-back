package com.neup.web.service;

import com.neup.web.model.Email;
import com.neup.web.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document; // ✅ Este es el import correcto
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecoverPasswordService {

    private final UsuarioRepository usuarioRepository;
    private final EnviarEmailService emailService;

    public void recuperarPassword(String nombreUsuario) {
        // 1. Buscar el usuario en MongoDB
        Document usuarioDoc = usuarioRepository.findByUsuario(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + nombreUsuario));

        // 2. Obtener el email del documento
        String emailDestino = usuarioDoc.getString("email");
        String pasword = usuarioDoc.getString("password");
        if (emailDestino == null || emailDestino.isBlank()) {
            throw new RuntimeException("El usuario no tiene email registrado");
        }

        // 3. Construir y enviar el correo
        Email email = Email.builder()
                .emailPara(emailDestino)
                .asunto("Recuperación de contraseña")
                .cuerpoEmail(construirCuerpo(nombreUsuario, pasword))
                .build();

        emailService.sendEmail(email);
    }

    private String construirCuerpo(String usuario, String contrasenna) {
        return """
                Hola %s,
                
                Recibimos una solicitud para recuperar tu contraseña.
                
                Tu nombre de usuario es: %s
                
                Si no solicitaste esto, ignora este mensaje.
                
                — Equipo de soporte
                """.formatted(usuario, contrasenna);
    }
}
