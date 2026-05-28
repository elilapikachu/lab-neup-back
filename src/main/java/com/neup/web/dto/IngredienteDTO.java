package com.neup.web.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

public class IngredienteDTO {

    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class IngredienteResponse {
        private String id;
        private String nombreIngrediente;
        private String tipoCantidad;
        private String tipoIngrediente;
    }
}
