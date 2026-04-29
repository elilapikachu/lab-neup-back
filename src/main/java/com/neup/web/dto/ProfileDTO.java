package com.neup.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO {
    private String usuarioId;
    private String username;
    private String email;

    private String personaId;

    // Datos personales
    private List<String> nombres;
    private List<String> apellidos;
    private Long telefono;
    private String otroEmail;

    // Físicos
    private Double peso;
    private Double altura;
    private Integer edad;

    // Objetivos
    private List<String> objetivos;

    // Actividad física
    private Integer frecuenciaSemanal;
    private List<String> tipoActividad;

    // Preferencias alimenticias
    private List<String> tipoDieta;
    private List<String> alergias;
    private List<String> gustos;
    private Integer comidasAlDia;
}
