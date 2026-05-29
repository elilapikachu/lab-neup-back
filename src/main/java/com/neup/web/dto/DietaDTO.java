package com.neup.web.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class DietaDTO {

    // ── Request: crear / actualizar dieta ───────────────────────────────────
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DietaRequest {

        @NotBlank(message = "El nombre de la dieta es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        private String nombreDieta;

        @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
        private String descripcion;

        private List<String> metas;

        @Valid
        private List<PlanSemanalRequest> planSemanal;

        private boolean esPersonalizada;

        @NotBlank(message = "La visibilidad es obligatoria")
        @Pattern(regexp = "^(publica|privada)$",
                 message = "La visibilidad debe ser 'publica' o 'privada'")
        private String visibilidad;

        @Size(max = 50, message = "El id del creador no puede superar los 50 caracteres")
        private String creadaPor;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PlanSemanalRequest {

        @NotBlank(message = "El id de la receta es obligatorio")
        private String recetaId;

        @NotBlank(message = "El tipo de comida es obligatorio")
        @Pattern(regexp = "^(desayuno|almuerzo|cena|merienda)$",
                 message = "El tipo de comida debe ser: desayuno, almuerzo, cena o merienda")
        private String tipoComida;

        @NotBlank(message = "El día es obligatorio")
        @Pattern(regexp = "^(lunes|martes|miercoles|jueves|viernes|sabado|domingo)$",
                 message = "El día debe ser: lunes, martes, miercoles, jueves, viernes, sabado o domingo")
        private String dia;
    }

    // ── Request: agregar/quitar receta del plan ──────────────────────────────
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class AgregarRecetaPlanRequest {

        @NotBlank(message = "El id de la receta es obligatorio")
        private String recetaId;

        @NotBlank(message = "El tipo de comida es obligatorio")
        @Pattern(regexp = "^(desayuno|almuerzo|cena|merienda)$",
                 message = "El tipo de comida debe ser: desayuno, almuerzo, cena o merienda")
        private String tipoComida;

        @NotBlank(message = "El día es obligatorio")
        @Pattern(regexp = "^(lunes|martes|miercoles|jueves|viernes|sabado|domingo)$",
                 message = "El día debe ser: lunes, martes, miercoles, jueves, viernes, sabado o domingo")
        private String dia;
    }

    // ── Response ─────────────────────────────────────────────────────────────
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DietaResponse {
        private String id;
        private String nombreDieta;
        private String descripcion;
        private List<String> metas;
        private List<PlanSemanalResponse> planSemanal;
        private boolean esPersonalizada;
        private String visibilidad;
        private String portada;
        private String creadaPor;
    }

    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PlanSemanalResponse {
        private String recetaId;
        private RecetaDTO.RecetaResponse receta;
        private String tipoComida;
        private String dia;
    }

    // ── Response genérica ────────────────────────────────────────────────────
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DietaIdResponse {
        private String id;
        private String mensaje;
    }

    // ── Response recomendadas ─────────────────────────────────────────────────
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class RecomendadasResponse {
        private boolean tienePreferencias;
        private List<DietaResponse> dietas;
    }
}
