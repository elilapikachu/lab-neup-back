package com.neup.web.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class RecetaDTO {

    // ── Request: crear / actualizar receta ──────────────────────────────────
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class RecetaRequest {

        @NotBlank(message = "El nombre de la receta es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        private String nombreReceta;

        @NotNull(message = "La lista de ingredientes es obligatoria")
        @Size(min = 1, message = "La receta debe tener al menos un ingrediente")
        @Valid
        private List<IngredienteRequest> ingredientes;

        @NotNull(message = "La información nutricional es obligatoria")
        @Valid
        private NutricionRequest nutricion;

        private List<String> tags;

        @Size(max = 50, message = "El tiempo de preparación no puede superar los 50 caracteres")
        private String tiempoPreparacion;

        private String creadaPor;

        private boolean esPersonalizada;

        @NotBlank(message = "La visibilidad es obligatoria")
        @Pattern(regexp = "^(publica|privada)$",
                 message = "La visibilidad debe ser 'publica' o 'privada'")
        private String visibilidad;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class IngredienteRequest {

        private String ingredienteId;

        @NotBlank(message = "El nombre del ingrediente es obligatorio")
        @Size(min = 1, max = 100, message = "El nombre del ingrediente no puede superar los 100 caracteres")
        private String nombreIngrediente;

        @Positive(message = "La cantidad debe ser mayor a 0")
        private double cantidad;

        @Size(max = 50, message = "El tipo de ingrediente no puede superar los 50 caracteres")
        private String tipoIngrediente;

        @Size(max = 50, message = "El tipo de cantidad no puede superar los 50 caracteres")
        private String tipoCantidad;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class NutricionRequest {

        @PositiveOrZero(message = "Las kcal no pueden ser negativas")
        private double kcal;

        @PositiveOrZero(message = "Las proteínas no pueden ser negativas")
        private double proteinas;

        @PositiveOrZero(message = "Los carbohidratos no pueden ser negativos")
        private double carbohidratos;

        @PositiveOrZero(message = "La fibra no puede ser negativa")
        private double fibra;

        private List<String> vitaminas;
        private List<String> minerales;
    }

    // ── Response ─────────────────────────────────────────────────────────────
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
        private List<String> imagen;
    }

    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class IngredienteResponse {
        private String ingredienteId;
        private String nombreIngrediente;
        private double cantidad;
        private String tipoIngrediente;
        private String tipoCantidad;
    }

    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class RecetaIdResponse {
        private String id;
        private String mensaje;
    }

    // ── Response recomendadas ─────────────────────────────────────────────────
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class RecomendadasResponse {
        private boolean tienePreferencias;
        private List<RecetaResponse> recetas;
    }
}
