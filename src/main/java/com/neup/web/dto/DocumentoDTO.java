package com.neup.web.dto;

import lombok.Builder;
import lombok.Data;

public class DocumentoDTO {

    @Data
    @Builder
    public static class DocumentoResponse {
        private String id;
        private String nombre;
        private long tamanno;
        private String ruta;
        private String extension;
        private String mensaje;
    }
}
