package com.neup.web.service;

import com.neup.web.model.Persona;
import com.neup.web.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ProfileService {
    private final PersonaRepository personaRepo;
    private final UsuarioRepository usuarioRepo;

    // ────────────────────────────────────────────────────────────────────────
    // GET  /api/persona/{usuarioId}
    // ────────────────────────────────────────────────────────────────────────
    public PerfilDTO obtenerPerfil(String usuarioId) {

        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado: " + usuarioId));

        // El perfil puede no existir todavía → devolvemos uno vacío
        Persona persona = personaRepo.findByUsuarioId(usuarioId)
                .orElse(null);

        return toDTO(usuario, persona);
    }

    // ────────────────────────────────────────────────────────────────────────
    // PUT  /api/persona/{usuarioId}
    // ────────────────────────────────────────────────────────────────────────
    public PerfilDTO guardarPerfil(String usuarioId, PerfilDTO dto) {

        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado: " + usuarioId));

        // Busca doc existente o crea uno nuevo
        Persona persona = personaRepo.findByUsuarioId(usuarioId)
                .orElse(Persona.builder()
                        .usuarioId(usuarioId)
                        .recetas(new Persona.Recetas(new ArrayList<>(), new ArrayList<>()))
                        .build());

        // Mapea DTO → entidad
        persona.setNombres(nullSafe(dto.getNombres()));
        persona.setApellidos(nullSafe(dto.getApellidos()));

        persona.setContactos(new Persona.Contactos(
                dto.getTelefono(),
                dto.getOtroEmail()
        ));

        persona.setCaracteristicasFisicas(new Persona.CaracteristicasFisicas(
                dto.getPeso(),
                dto.getAltura()
        ));

        persona.setPreferencias(new Persona.Preferencias(
                nullSafe(dto.getGustos()),
                nullSafe(dto.getAlergias()),
                nullSafe(dto.getTipoDieta()),
                nullSafe(dto.getObjetivos())
        ));

        persona.setActividadFisica(new Persona.ActividadFisica(
                dto.getFrecuenciaSemanal(),
                nullSafe(dto.getTipoActividad())
        ));

        Persona guardada = personaRepo.save(persona);
        return toDTO(usuario, guardada);
    }

    // ────────────────────────────────────────────────────────────────────────
    // Helpers
    // ────────────────────────────────────────────────────────────────────────
    private PerfilDTO toDTO(Usuario usuario, Persona persona) {
        PerfilDTO dto = new PerfilDTO();
        dto.setUsuarioId(usuario.getId());
        dto.setUsername(usuario.getUsuario());
        dto.setEmail(usuario.getEmail());

        if (persona != null) {
            dto.setPersonaId(persona.getId());
            dto.setNombres(persona.getNombres());
            dto.setApellidos(persona.getApellidos());

            if (persona.getContactos() != null) {
                dto.setTelefono(persona.getContactos().getTelefono());
                dto.setOtroEmail(persona.getContactos().getOtroEmail());
            }

            if (persona.getCaracteristicasFisicas() != null) {
                dto.setPeso(persona.getCaracteristicasFisicas().getPeso());
                dto.setAltura(persona.getCaracteristicasFisicas().getAltura());
            }

            if (persona.getPreferencias() != null) {
                dto.setGustos(persona.getPreferencias().getGustos());
                dto.setAlergias(persona.getPreferencias().getAlergias());
                dto.setTipoDieta(persona.getPreferencias().getTipoDieta());
                dto.setObjetivos(persona.getPreferencias().getObjetivos());
            }

            if (persona.getActividadFisica() != null) {
                dto.setFrecuenciaSemanal(persona.getActividadFisica().getFrecuenciaSemanal());
                dto.setTipoActividad(persona.getActividadFisica().getTipoActividad());
            }
        }

        return dto;
    }

    private <T> List<T> nullSafe(List<T> list) {
        return list != null ? list : new ArrayList<>();
    }
}
