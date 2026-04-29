package com.neup.web.model;

import org.bson.types.ObjectId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Persona {

    private String id;
    private ObjectId usuarioId;
    private List<String> nombres;
    private List<String> apellidos;
    private Contactos contactos;
    private CaracteristicasFisicas caracteristicasFisicas;
    private Preferencias preferencias;
    private ActividadFisica actividadFisica;
    private Dietas dietas;
    private Recetas recetas;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Contactos {
        private Long telefono;
        private String otroEmail;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CaracteristicasFisicas {
        private Double peso;
        private Double altura;
        private Integer edad;  // ✅ Agregado
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Preferencias {
        private List<String> gustos;
        private List<String> alergias;
        private List<String> tipoDieta;
        private List<String> objetivos;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActividadFisica {
        private Integer frecuenciaSemanal;
        private List<String> tipoActividad;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dietas {
        private List<String> guardadas;
        private List<String> personalizadas;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Recetas {
        private List<String> guardadas;
        private List<String> personalizadas;
    }
}