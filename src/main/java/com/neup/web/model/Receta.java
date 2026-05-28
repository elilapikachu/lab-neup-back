package com.neup.web.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@Builder
public class Receta {

    private ObjectId id;
    private String nombreReceta;
    private List<Ingrediente> ingredientes;
    private Nutricion nutricion;
    private List<String> tags;
    private String tiempoPreparacion;
    private ObjectId creadaPor;
    private boolean esPersonalizada;
    private String visibilidad; // "publica" | "privada"
    private List<ObjectId> imagen;

    @Data
    @Builder
    public static class Ingrediente {
        private String ingredienteId;
        private String nombreIngrediente;
        private double cantidad;
        private String tipoIngrediente;
        private String tipoCantidad;
    }

    @Data
    @Builder
    public static class Nutricion {
        private double kcal;
        private double proteinas;
        private double carbohidratos;
        private double fibra;
        private List<String> vitaminas;
        private List<String> minerales;
    }
}
