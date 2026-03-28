package com.neup.web.dto;

// ── Request: Login ───────────────────────────────────────────
public class AuthDTO {

    public static class LoginRequest {
        private String usuario;
        private String password;

        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // ── Request: Registro ────────────────────────────────────
    public static class RegisterRequest {
        // Datos de usuario
        private String usuario;
        private String email;
        private String password;

        // Datos de persona
        private String nombre;
        private String apellido;
        private Long telefono;
        private Double peso;
        private Double altura;

        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getApellido() { return apellido; }
        public void setApellido(String apellido) { this.apellido = apellido; }

        public Long getTelefono() { return telefono; }
        public void setTelefono(Long telefono) { this.telefono = telefono; }

        public Double getPeso() { return peso; }
        public void setPeso(Double peso) { this.peso = peso; }

        public Double getAltura() { return altura; }
        public void setAltura(Double altura) { this.altura = altura; }
    }

    // ── Response genérico ────────────────────────────────────
    public static class AuthResponse {
        private boolean success;
        private String message;
        private String usuarioId;
        private String personaId;
        private String nombreUsuario;
        private String email;

        public AuthResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public String getUsuarioId() { return usuarioId; }
        public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

        public String getPersonaId() { return personaId; }
        public void setPersonaId(String personaId) { this.personaId = personaId; }

        public String getNombreUsuario() { return nombreUsuario; }
        public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}
