package com.neup.web.dto;

import lombok.Getter;
import lombok.Setter;

public class AuthDTO {

    @Setter
    @Getter
    public static class LoginRequest {
        private String usuario;
        private String password;
    }

    @Setter
    @Getter
    public static class RegisterRequest {
        private String usuario;
        private String email;
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
