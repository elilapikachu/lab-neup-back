package com.neup.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class RecetaDTO {

    // ── Request: crear / actualizar receta ──────────────────────────────────
    @Data
    public static class RecetaRequest {
        private String nombreReceta;
        private List<IngredienteRequest> ingredientes;
        private NutricionRequest nutricion;
        private List<String> tags;
        private String tiempoPreparacion;
        private String creadaPor;       // personaId (ObjectId string), null si es del sistema
        private boolean esPersonalizada;
        private String visibilidad;     // "publica" | "privada"
    }

    @Data
    public static class IngredienteRequest {
        private String nombreIngrediente;
        private double cantidad;
        private String tipoIngrediente;
    }

    @Data
    public static class NutricionRequest {
        private double kcal;
        private double proteinas;
        private double carbohidratos;
        private double fibra;
        private List<String> vitaminas;
        private List<String> minerales;
    }

    // ── Response ─────────────────────────────────────────────────────────────
    @Data
    @Builder
    public static class RecetaResponse {
        private String id;
        private String nombreReceta;
        private List<IngredienteResponse> ingredientes;
        private NutricionResponse nutricion;
        private List<String> tags;
        private String tiempoPreparacion;
        private String creadaPor;
        private boolean esPersonalizada;
        private String visibilidad;
        private List<String> imagen; // lista de documentoIds
    }

    @Data
    @Builder
    public static class IngredienteResponse {
        private String nombreIngrediente;
        private double cantidad;
        private String tipoIngrediente;
    }

    @Data
    @Builder
    public static class NutricionResponse {
        private double kcal;
        private double proteinas;
        private double carbohidratos;
        private double fibra;
        private List<String> vitaminas;
        private List<String> minerales;
    }

    // ── Response genérica ────────────────────────────────────────────────────
    @Data
    @Builder
    public static class RecetaIdResponse {
        private String id;
        private String mensaje;
    }
}
