package com.neup.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class DietaDTO {

    // ── Request: crear / actualizar dieta ───────────────────────────────────
    @Data
    public static class DietaRequest {
        private String nombreDieta;
        private String descripcion;
        private List<String> metas;
        private List<PlanSemanalRequest> planSemanal;
        private boolean esPersonalizada;
        private String visibilidad; // "publica" | "privada"
    }

    @Data
    public static class PlanSemanalRequest {
        private String recetaId;
        private String tipoComida; // desayuno | almuerzo | cena | merienda
    }

    // ── Request: agregar/quitar receta del plan ──────────────────────────────
    @Data
    public static class AgregarRecetaPlanRequest {
        private String recetaId;
        private String tipoComida;
    }

    // ── Response ─────────────────────────────────────────────────────────────
    @Data
    @Builder
    public static class DietaResponse {
        private String id;
        private String nombreDieta;
        private String descripcion;
        private List<String> metas;
        private List<PlanSemanalResponse> planSemanal;
        private boolean esPersonalizada;
        private String visibilidad;
        private String portada; // documentoId
    }

    @Data
    @Builder
    public static class PlanSemanalResponse {
        private String recetaId;
        private String tipoComida;
    }

    // ── Response genérica ────────────────────────────────────────────────────
    @Data
    @Builder
    public static class DietaIdResponse {
        private String id;
        private String mensaje;
    }
}
