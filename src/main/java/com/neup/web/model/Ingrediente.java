package com.neup.web.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@Builder
public class Ingrediente {

    private ObjectId id;
    private String nombreIngrediente;
    private String tipoCantidad;
    private String tipoIngrediente;
}
