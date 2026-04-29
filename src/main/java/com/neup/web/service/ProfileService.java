package com.neup.web.service;

import com.neup.web.dto.ProfileDTO;
import com.neup.web.repository.PersonaRepository;
import com.neup.web.repository.UsuarioRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final PersonaRepository personaRepo;
    private final UsuarioRepository usuarioRepo;

    public ProfileDTO obtenerPerfil(String usuarioId) {
        // Verificar que el usuario existe
        Document usuarioDoc = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Si el perfil no existe, devolver perfil vacío (NO error)
        Document personaDoc = personaRepo.findByUsuarioId(usuarioId).orElse(null);

        return toDTO(usuarioDoc, personaDoc);
    }

    public ProfileDTO guardarPerfil(String usuarioId, ProfileDTO dto) {
        Document usuarioDoc = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Document personaExistente = personaRepo.findByUsuarioId(usuarioId).orElse(null);

        Document contactos = new Document()
                .append("telefono", dto.getTelefono())
                .append("otro_email", dto.getOtroEmail());

        Document caracteristicas = new Document()
                .append("peso", dto.getPeso())
                .append("altura", dto.getAltura())
                .append("edad", dto.getEdad());

        Document preferencias = new Document()
                .append("gustos", nullSafe(dto.getGustos()))
                .append("alergias", nullSafe(dto.getAlergias()))
                .append("tipo_dieta", nullSafe(dto.getTipoDieta()))
                .append("objetivos", nullSafe(dto.getObjetivos()));

        Document actividadFisica = new Document()
                .append("frecuencia_semanal", dto.getFrecuenciaSemanal())
                .append("tipo_actividad", nullSafe(dto.getTipoActividad()));

        Document updateDoc = new Document()
                .append("nombres", nullSafe(dto.getNombres()))
                .append("apellidos", nullSafe(dto.getApellidos()))
                .append("contactos", contactos)
                .append("caracteristicas_fisicas", caracteristicas)
                .append("preferencias", preferencias)
                .append("actividad_fisica", actividadFisica)
                .append("comidas_al_dia", dto.getComidasAlDia());

        if (personaExistente != null) {
            // Actualizar
            String personaId = personaExistente.getObjectId("_id").toHexString();
            personaRepo.actualizar(personaId, updateDoc);
        } else {
            // Crear nuevo
            updateDoc.append("usuario_id", new ObjectId(usuarioId));
            personaRepo.insertar(new com.neup.web.model.Persona());
        }

        Document personaActualizada = personaRepo.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error al guardar el perfil"));

        return toDTO(usuarioDoc, personaActualizada);
    }

    private ProfileDTO toDTO(Document usuarioDoc, Document personaDoc) {
        ProfileDTO dto = new ProfileDTO();

        dto.setUsuarioId(usuarioDoc.getObjectId("_id").toHexString());
        dto.setUsername(usuarioDoc.getString("usuario"));
        dto.setEmail(usuarioDoc.getString("email"));

        if (personaDoc != null) {
            dto.setPersonaId(personaDoc.getObjectId("_id").toHexString());
            dto.setNombres(personaDoc.getList("nombres", String.class));
            dto.setApellidos(personaDoc.getList("apellidos", String.class));

            Document contactos = (Document) personaDoc.get("contactos");
            if (contactos != null) {
                dto.setTelefono(contactos.getLong("telefono"));
                dto.setOtroEmail(contactos.getString("otro_email"));
            }

            Document caracteristicas = (Document) personaDoc.get("caracteristicas_fisicas");
            if (caracteristicas != null) {
                dto.setPeso(caracteristicas.getDouble("peso"));
                dto.setAltura(caracteristicas.getDouble("altura"));
                dto.setEdad(caracteristicas.getInteger("edad"));
            }

            Document preferencias = (Document) personaDoc.get("preferencias");
            if (preferencias != null) {
                dto.setGustos(preferencias.getList("gustos", String.class));
                dto.setAlergias(preferencias.getList("alergias", String.class));
                dto.setTipoDieta(preferencias.getList("tipo_dieta", String.class));
                dto.setObjetivos(preferencias.getList("objetivos", String.class));
            }

            Document actividadFisica = (Document) personaDoc.get("actividad_fisica");
            if (actividadFisica != null) {
                dto.setFrecuenciaSemanal(actividadFisica.getInteger("frecuencia_semanal"));
                dto.setTipoActividad(actividadFisica.getList("tipo_actividad", String.class));
            }

            dto.setComidasAlDia(personaDoc.getInteger("comidas_al_dia"));
        }

        return dto;
    }

    private <T> List<T> nullSafe(List<T> list) {
        return list != null ? list : new ArrayList<>();
    }
}