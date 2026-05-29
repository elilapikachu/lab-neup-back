package com.neup.web.service;

import com.neup.web.dto.DietaDTO;
import com.neup.web.dto.RecetaDTO;
import com.neup.web.model.Dieta;
import com.neup.web.repository.DietaRepository;
import com.neup.web.repository.PersonaRepository;
import com.neup.web.utils.repository.ConstantesDietaRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class DietaService {

    private final DietaRepository dietaRepository;
    private final DocumentoService documentoService;
    private final RecetaService recetaService;
    private final PersonaRepository personaRepository;

    public DietaService(DietaRepository dietaRepository, DocumentoService documentoService,
                        @Lazy RecetaService recetaService, PersonaRepository personaRepository) {
        this.dietaRepository  = dietaRepository;
        this.documentoService = documentoService;
        this.recetaService    = recetaService;
        this.personaRepository = personaRepository;
    }

    // ── Crear ─────────────────────────────────────────────────────────────────

    public DietaDTO.DietaIdResponse crear(DietaDTO.DietaRequest request) {
        Dieta dieta = mapearRequest(request);
        ObjectId id = dietaRepository.insertar(dieta);
        return DietaDTO.DietaIdResponse.builder()
                .id(id.toHexString())
                .mensaje("Dieta creada correctamente")
                .build();
    }

    // ── Obtener ───────────────────────────────────────────────────────────────

    public Optional<DietaDTO.DietaResponse> obtenerPorId(String id) {
        return dietaRepository.findById(id).map(this::mapearResponse);
    }

    public List<DietaDTO.DietaResponse> obtenerTodas() {
        return dietaRepository.findAll().stream()
                .map(this::mapearResponse)
                .toList();
    }

    public List<DietaDTO.DietaResponse> obtenerPublicas() {
        return dietaRepository.findByVisibilidad(ConstantesDietaRepository.VISIBILIDAD_PUBLICA)
                .stream().map(this::mapearResponse).toList();
    }

    public List<DietaDTO.DietaResponse> obtenerPorPersona(String personaId) {
        return dietaRepository.findByPersonaId(personaId)
                .stream().map(this::mapearResponse).toList();
    }

    // ── Actualizar ────────────────────────────────────────────────────────────

    public boolean actualizar(String id, DietaDTO.DietaRequest request) {
        Document campos = new Document();

        if (request.getNombreDieta()  != null) campos.append(ConstantesDietaRepository.CAMPO_NOMBRE_DIETA,  request.getNombreDieta());
        if (request.getDescripcion()  != null) campos.append(ConstantesDietaRepository.CAMPO_DESCRIPCION,   request.getDescripcion());
        if (request.getMetas()        != null) campos.append(ConstantesDietaRepository.CAMPO_METAS,         request.getMetas());
        if (request.getVisibilidad()  != null) campos.append(ConstantesDietaRepository.CAMPO_VISIBILIDAD,   request.getVisibilidad());

        if (request.getPlanSemanal() != null) {
            List<Document> planDoc = request.getPlanSemanal().stream()
                    .map(p -> new Document()
                            .append(ConstantesDietaRepository.CAMPO_RECETA_ID,   new ObjectId(p.getRecetaId()))
                            .append(ConstantesDietaRepository.CAMPO_TIPO_COMIDA, p.getTipoComida())
                            .append(ConstantesDietaRepository.CAMPO_DIA,         p.getDia()))
                    .toList();
            campos.append(ConstantesDietaRepository.CAMPO_PLAN_SEMANAL, planDoc);
        }

        if (campos.isEmpty()) return false;
        return dietaRepository.actualizar(id, campos);
    }

    // ── Plan semanal ──────────────────────────────────────────────────────────

    public boolean agregarRecetaAlPlan(String dietaId, DietaDTO.AgregarRecetaPlanRequest request) {
        return dietaRepository.agregarRecetaPlan(dietaId, new ObjectId(request.getRecetaId()), request.getTipoComida(), request.getDia());
    }

    public boolean eliminarRecetaDelPlan(String dietaId, String recetaId) {
        return dietaRepository.eliminarRecetaPlan(dietaId, new ObjectId(recetaId));
    }

    // ── Portada ───────────────────────────────────────────────────────────────

    public DietaDTO.DietaIdResponse subirPortada(String dietaId, MultipartFile archivo, String nombre) throws IOException {
        var docResponse = documentoService.guardarImagen(archivo, nombre);
        dietaRepository.actualizarPortada(dietaId, new ObjectId(docResponse.getId()));
        return DietaDTO.DietaIdResponse.builder()
                .id(docResponse.getId())
                .mensaje("Portada actualizada correctamente")
                .build();
    }

    // ── Recomendadas ──────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    public DietaDTO.RecomendadasResponse obtenerRecomendadas(String personaId) {
        Document personaDoc = personaRepository.findById(personaId).orElse(null);

        List<String> tipoDieta = List.of();
        List<String> objetivos = List.of();

        if (personaDoc != null) {
            Document prefs = (Document) personaDoc.get("preferencias");
            if (prefs != null) {
                tipoDieta = safeStringList(prefs, "tipo_dieta");
                objetivos = safeStringList(prefs, "objetivos");
            }
        }

        boolean tienePreferencias = !tipoDieta.isEmpty() || !objetivos.isEmpty();

        if (!tienePreferencias) {
            return DietaDTO.RecomendadasResponse.builder()
                    .tienePreferencias(false)
                    .dietas(List.of())
                    .build();
        }

        final List<String> td = tipoDieta;
        final List<String> ob = objetivos;

        record Scored(int score, DietaDTO.DietaResponse dieta) {}

        List<DietaDTO.DietaResponse> recomendadas = dietaRepository
                .findByVisibilidad(ConstantesDietaRepository.VISIBILIDAD_PUBLICA)
                .stream()
                .map(doc -> new Scored(puntajeDieta(doc, td, ob), mapearResponse(doc)))
                .sorted(Comparator.comparingInt(Scored::score).reversed())
                .map(Scored::dieta)
                .limit(6)
                .toList();

        return DietaDTO.RecomendadasResponse.builder()
                .tienePreferencias(true)
                .dietas(recomendadas)
                .build();
    }

    @SuppressWarnings("unchecked")
    private int puntajeDieta(Document doc, List<String> tipoDieta, List<String> objetivos) {
        int score = 0;

        List<String> metas = (List<String>) doc.get(ConstantesDietaRepository.CAMPO_METAS);
        if (metas != null) {
            for (String meta : metas) {
                String metaLower = meta.toLowerCase();
                for (String obj : objetivos) {
                    if (metaLower.contains(obj.toLowerCase()) || obj.toLowerCase().contains(metaLower)) {
                        score += 3;
                    }
                }
            }
        }

        String nombreDieta  = doc.getString(ConstantesDietaRepository.CAMPO_NOMBRE_DIETA);
        String descripcion  = doc.getString(ConstantesDietaRepository.CAMPO_DESCRIPCION);
        String textoLower   = ((nombreDieta != null ? nombreDieta : "") + " " +
                               (descripcion  != null ? descripcion  : "")).toLowerCase();

        for (String tipo : tipoDieta) {
            if (textoLower.contains(tipo.toLowerCase())) score += 2;
        }

        return score;
    }

    @SuppressWarnings("unchecked")
    private static List<String> safeStringList(Document doc, String key) {
        List<String> list = (List<String>) doc.get(key);
        return list != null ? list : List.of();
    }

    // ── Eliminar ──────────────────────────────────────────────────────────────

    public boolean eliminar(String id) {
        return dietaRepository.eliminar(id);
    }

    // ── Helpers de lectura robusta ────────────────────────────────────────────

    /**
     * Lee un campo que puede estar almacenado como ObjectId o String
     * y lo devuelve siempre como hex String (o null).
     */
    private static String getIdAsString(Document doc, String key) {
        Object val = doc.get(key);
        if (val == null) return null;
        if (val instanceof ObjectId oid) return oid.toHexString();
        return val.toString();
    }

    // ── Mappers ───────────────────────────────────────────────────────────────

    private Dieta mapearRequest(DietaDTO.DietaRequest request) {
        List<Dieta.PlanSemanal> plan = new ArrayList<>();
        if (request.getPlanSemanal() != null) {
            for (var p : request.getPlanSemanal()) {
                plan.add(Dieta.PlanSemanal.builder()
                        .recetaId(new ObjectId(p.getRecetaId()))
                        .tipoComida(p.getTipoComida())
                        .dia(p.getDia())
                        .build());
            }
        }

        return Dieta.builder()
                .nombreDieta(request.getNombreDieta())
                .descripcion(request.getDescripcion())
                .metas(request.getMetas())
                .planSemanal(plan)
                .esPersonalizada(request.isEsPersonalizada())
                .visibilidad(request.getVisibilidad())
                .creadaPor(request.getCreadaPor())
                .build();
    }

    @SuppressWarnings("unchecked")
    private DietaDTO.DietaResponse mapearResponse(Document doc) {
        List<DietaDTO.PlanSemanalResponse> plan = new ArrayList<>();
        List<Document> planDocs = (List<Document>) doc.get(ConstantesDietaRepository.CAMPO_PLAN_SEMANAL);
        if (planDocs != null) {
            for (Document p : planDocs) {
                String recetaId = getIdAsString(p, ConstantesDietaRepository.CAMPO_RECETA_ID);
                RecetaDTO.RecetaResponse receta = recetaId != null
                        ? recetaService.obtenerPorId(recetaId).orElse(null)
                        : null;
                plan.add(DietaDTO.PlanSemanalResponse.builder()
                        .recetaId(recetaId)
                        .receta(receta)
                        .tipoComida(p.getString(ConstantesDietaRepository.CAMPO_TIPO_COMIDA))
                        .dia(p.getString(ConstantesDietaRepository.CAMPO_DIA))
                        .build());
            }
        }

        return DietaDTO.DietaResponse.builder()
                .id(getIdAsString(doc, ConstantesDietaRepository.CAMPO_ID))
                .nombreDieta(doc.getString(ConstantesDietaRepository.CAMPO_NOMBRE_DIETA))
                .descripcion(doc.getString(ConstantesDietaRepository.CAMPO_DESCRIPCION))
                .metas((List<String>) doc.get(ConstantesDietaRepository.CAMPO_METAS))
                .planSemanal(plan)
                .esPersonalizada(Boolean.TRUE.equals(doc.getBoolean(ConstantesDietaRepository.CAMPO_ES_PERSONALIZADA)))
                .visibilidad(doc.getString(ConstantesDietaRepository.CAMPO_VISIBILIDAD))
                .portada(getIdAsString(doc, ConstantesDietaRepository.CAMPO_PORTADA))
                .creadaPor(getIdAsString(doc, ConstantesDietaRepository.CAMPO_CREADA_POR))
                .build();
    }
}
