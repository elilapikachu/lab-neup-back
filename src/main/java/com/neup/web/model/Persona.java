package com.neup.web.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import java.util.List;

@Setter
@Getter
public class Persona {

    private ObjectId id;
    private List<String> nombres;
    private List<String> apellidos;
    private Contactos contactos;
    private CaracteristicasFisicas caracteristicasFisicas;
    private Preferencias preferencias;
    private ObjectId usuarioId;
    private Dietas dietas;
    private Recetas recetas;

    @Setter
    @Getter
    public static class Contactos {
        private Long telefono;
        private String otroEmail;
    }

    @Setter
    @Getter
    public static class CaracteristicasFisicas {
        private Double peso;
        private Double altura;
    }

    @Setter
    @Getter
    public static class Preferencias {
        private List<String> gustos;
        private List<String> alergias;
        private List<String> tipoDieta;
        private List<String> objetivos;
    }

    @Setter
    @Getter
    public static class Dietas {
        private List<ObjectId> guardadas;
        private List<ObjectId> personalizadas;
    }

    @Setter
    @Getter
    public static class Recetas {
        private List<ObjectId> guardadas;
        private List<ObjectId> personalizadas;
    }
}
