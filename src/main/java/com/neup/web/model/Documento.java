package com.neup.web.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@Builder
public class Documento {

    private ObjectId id;
    private String nombre;
    private long tamanno;
    private String ruta;
    private String extension;
}
