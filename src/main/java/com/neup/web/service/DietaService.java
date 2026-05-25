package com.neup.web.service;

import com.neup.web.dto.DietaDTO;
import com.neup.web.model.Dieta;
import com.neup.web.repository.DietaRepository;
import com.neup.web.utils.repository.ConstantesDietaRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DietaService {

    private final DietaRepository dietaRepository;
    private final DocumentoService documentoService;

    public DietaService(DietaRepository dietaRepository, DocumentoService documentoService) {
        this.dietaRepository  = dietaRepository;
        this.documentoService = documentoService;
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
                            .append(ConstantesDietaRepository.CAMPO_TIPO_COMIDA, p.getTipoComida()))
                    .toList();
            campos.append(ConstantesDietaRepository.CAMPO_PLAN_SEMANAL, planDoc);
        }

        if (campos.isEmpty()) return false;
        return dietaRepository.actualizar(id, campos);
    }

    // ── Plan semanal ──────────────────────────────────────────────────────────

    public boolean agregarRecetaAlPlan(String dietaId, DietaDTO.AgregarRecetaPlanRequest request) {
        return dietaRepository.agregarRecetaPlan(dietaId, new ObjectId(request.getRecetaId()), request.getTipoComida());
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

    // ── Eliminar ──────────────────────────────────────────────────────────────

    public boolean eliminar(String id) {
        return dietaRepository.eliminar(id);
    }

    // ── Mappers ───────────────────────────────────────────────────────────────

    private Dieta mapearRequest(DietaDTO.DietaRequest request) {
        List<Dieta.PlanSemanal> plan = new ArrayList<>();
        if (request.getPlanSemanal() != null) {
            for (var p : request.getPlanSemanal()) {
                plan.add(Dieta.PlanSemanal.builder()
                        .recetaId(new ObjectId(p.getRecetaId()))
                        .tipoComida(p.getTipoComida())
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
                .build();
    }

    @SuppressWarnings("unchecked")
    private DietaDTO.DietaResponse mapearResponse(Document doc) {
        List<DietaDTO.PlanSemanalResponse> plan = new ArrayList<>();
        List<Document> planDocs = (List<Document>) doc.get(ConstantesDietaRepository.CAMPO_PLAN_SEMANAL);
        if (planDocs != null) {
            for (Document p : planDocs) {
                ObjectId recetaId = p.getObjectId(ConstantesDietaRepository.CAMPO_RECETA_ID);
                plan.add(DietaDTO.PlanSemanalResponse.builder()
                        .recetaId(recetaId != null ? recetaId.toHexString() : null)
                        .tipoComida(p.getString(ConstantesDietaRepository.CAMPO_TIPO_COMIDA))
                        .build());
            }
        }

        ObjectId portada = doc.getObjectId(ConstantesDietaRepository.CAMPO_PORTADA);

        return DietaDTO.DietaResponse.builder()
                .id(doc.getObjectId(ConstantesDietaRepository.CAMPO_ID).toHexString())
                .nombreDieta(doc.getString(ConstantesDietaRepository.CAMPO_NOMBRE_DIETA))
                .descripcion(doc.getString(ConstantesDietaRepository.CAMPO_DESCRIPCION))
                .metas((List<String>) doc.get(ConstantesDietaRepository.CAMPO_METAS))
                .planSemanal(plan)
                .esPersonalizada(Boolean.TRUE.equals(doc.getBoolean(ConstantesDietaRepository.CAMPO_ES_PERSONALIZADA)))
                .visibilidad(doc.getString(ConstantesDietaRepository.CAMPO_VISIBILIDAD))
                .portada(portada != null ? portada.toHexString() : null)
                .build();
    }
}
