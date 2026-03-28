package com.neup.web.service;

import com.neup.web.dto.AuthDTO.*;
import com.neup.web.model.Persona;
import com.neup.web.model.Usuario;
import com.neup.web.repository.PersonaRepository;
import com.neup.web.repository.UsuarioRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PersonaRepository personaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // ── LOGIN ────────────────────────────────────────────────
    public AuthResponse login(LoginRequest request) {
        if (request.getUsuario() == null || request.getPassword() == null) {
            return new AuthResponse(false, "Usuario y contraseña son obligatorios");
        }

        Optional<Document> usuarioDoc = usuarioRepository.findByUsuario(request.getUsuario());

        if (usuarioDoc.isEmpty()) {
            return new AuthResponse(false, "Usuario o contraseña incorrectos");
        }

        Document doc = usuarioDoc.get();
        String passwordGuardada = doc.getString("password");

        if (!passwordEncoder.matches(request.getPassword(), passwordGuardada)) {
            return new AuthResponse(false, "Usuario o contraseña incorrectos");
        }

        String usuarioId = doc.getObjectId("_id").toHexString();

        // Buscar persona asociada
        Optional<Document> personaDoc = personaRepository.findByUsuarioId(usuarioId);

        AuthResponse response = new AuthResponse(true, "Login exitoso");
        response.setUsuarioId(usuarioId);
        response.setNombreUsuario(doc.getString("usuario"));
        response.setEmail(doc.getString("email"));

        personaDoc.ifPresent(p -> response.setPersonaId(p.getObjectId("_id").toHexString()));

        return response;
    }

    // ── REGISTRO ─────────────────────────────────────────────
    public AuthResponse registro(RegisterRequest request) {
        // Validaciones básicas
        if (request.getUsuario() == null || request.getEmail() == null || request.getPassword() == null) {
            return new AuthResponse(false, "Usuario, email y contraseña son obligatorios");
        }

        if (request.getNombre() == null || request.getApellido() == null) {
            return new AuthResponse(false, "Nombre y apellido son obligatorios");
        }

        if (request.getPassword().length() < 6) {
            return new AuthResponse(false, "La contraseña debe tener al menos 6 caracteres");
        }

        // Verificar si ya existe usuario o email
        if (usuarioRepository.existeUsuarioOEmail(request.getUsuario(), request.getEmail())) {
            return new AuthResponse(false, "El usuario o email ya está registrado");
        }

        // Hashear password
        String passwordHasheada = passwordEncoder.encode(request.getPassword());

        // Crear y guardar usuario
        Usuario nuevoUsuario = new Usuario(
                request.getUsuario(),
                request.getEmail(),
                passwordHasheada
        );

        ObjectId usuarioId = usuarioRepository.insertar(nuevoUsuario);

        // Crear y guardar persona vinculada
        Persona nuevaPersona = new Persona();
        nuevaPersona.setNombres(List.of(request.getNombre()));
        nuevaPersona.setApellidos(List.of(request.getApellido()));
        nuevaPersona.setUsuarioId(usuarioId);

        // Contactos
        Persona.Contactos contactos = new Persona.Contactos();
        contactos.setTelefono(request.getTelefono());
        contactos.setOtroEmail(request.getEmail());
        nuevaPersona.setContactos(contactos);

        // Características físicas
        if (request.getPeso() != null || request.getAltura() != null) {
            Persona.CaracteristicasFisicas cf = new Persona.CaracteristicasFisicas();
            cf.setPeso(request.getPeso());
            cf.setAltura(request.getAltura());
            nuevaPersona.setCaracteristicasFisicas(cf);
        }

        ObjectId personaId = personaRepository.insertar(nuevaPersona);

        AuthResponse response = new AuthResponse(true, "Registro exitoso");
        response.setUsuarioId(usuarioId.toHexString());
        response.setPersonaId(personaId.toHexString());
        response.setNombreUsuario(request.getUsuario());
        response.setEmail(request.getEmail());

        return response;
    }

    // ── CAMBIAR PASSWORD ─────────────────────────────────────
    public AuthResponse cambiarPassword(String usuarioId, String passwordActual, String nuevaPassword) {
        Optional<Document> usuarioDoc = usuarioRepository.findById(usuarioId);

        if (usuarioDoc.isEmpty()) {
            return new AuthResponse(false, "Usuario no encontrado");
        }

        String passwordGuardada = usuarioDoc.get().getString("password");

        if (!passwordEncoder.matches(passwordActual, passwordGuardada)) {
            return new AuthResponse(false, "La contraseña actual es incorrecta");
        }

        if (nuevaPassword.length() < 6) {
            return new AuthResponse(false, "La nueva contraseña debe tener al menos 6 caracteres");
        }

        String nuevaPasswordHasheada = passwordEncoder.encode(nuevaPassword);
        boolean actualizado = usuarioRepository.actualizarPassword(usuarioId, nuevaPasswordHasheada);

        return actualizado
                ? new AuthResponse(true, "Contraseña actualizada correctamente")
                : new AuthResponse(false, "No se pudo actualizar la contraseña");
    }

    // ── ELIMINAR CUENTA ──────────────────────────────────────
    public AuthResponse eliminarCuenta(String usuarioId) {
        // Eliminar persona primero
        Optional<Document> personaDoc = personaRepository.findByUsuarioId(usuarioId);
        personaDoc.ifPresent(p ->
                personaRepository.eliminar(p.getObjectId("_id").toHexString())
        );

        // Eliminar usuario
        boolean eliminado = usuarioRepository.eliminar(usuarioId);

        return eliminado
                ? new AuthResponse(true, "Cuenta eliminada correctamente")
                : new AuthResponse(false, "No se pudo eliminar la cuenta");
    }
}
