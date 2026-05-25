package com.neup.web.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@Builder
public class Dieta {

    private ObjectId id;
    private String nombreDieta;
    private String descripcion;
    private List<String> metas;
    private List<PlanSemanal> planSemanal;
    private boolean esPersonalizada;
    private String visibilidad; // "publica" | "privada"
    private ObjectId portada;

    @Data
    @Builder
    public static class PlanSemanal {
        private ObjectId recetaId;
        private String tipoComida; // desayuno | almuerzo | cena | merienda
    }
}

