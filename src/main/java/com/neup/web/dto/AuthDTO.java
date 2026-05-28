package com.neup.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class AuthDTO {

    @Setter
    @Getter
    public static class LoginRequest {

        @NotBlank(message = "El usuario es obligatorio")
        @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
        private String usuario;

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
        private String password;
    }

    @Setter
    @Getter
    public static class RegisterRequest {

        @NotBlank(message = "El usuario es obligatorio")
        @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$",
                 message = "El usuario solo puede contener letras, números y guión bajo")
        private String usuario;

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        @Size(max = 100, message = "El email no puede superar los 100 caracteres")
        private String email;

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
        private String password;
    }

    @Setter
    @Getter
    public static class AuthResponse {
        private boolean success;
        private String message;
        private String usuarioId;
        private String personaId;
        private String nombreUsuario;
        private String email;
        private boolean passwordTemporal;

        public AuthResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}
