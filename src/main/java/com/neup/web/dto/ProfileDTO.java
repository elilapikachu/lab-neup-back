package com.neup.web.dto;

import jakarta.validation.constraints.*;
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

    // IDs — solo lectura, sin validar en update
    private String usuarioId;
    private String personaId;

    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$",
             message = "El usuario solo puede contener letras, números y guión bajo")
    private String username;

    @Email(message = "El email no tiene un formato válido")
    @Size(max = 100, message = "El email no puede superar los 100 caracteres")
    private String email;

    // Datos personales
    private List<@NotBlank(message = "El nombre no puede estar vacío") String> nombres;
    private List<@NotBlank(message = "El apellido no puede estar vacío") String> apellidos;

    @Min(value = 0, message = "El teléfono no puede ser negativo")
    private Long telefono;

    @Email(message = "El email alternativo no tiene un formato válido")
    @Size(max = 100, message = "El email alternativo no puede superar los 100 caracteres")
    private String otroEmail;

    // Físicos
    @DecimalMin(value = "0.0", message = "El peso no puede ser negativo")
    @DecimalMax(value = "500.0", message = "El peso no puede superar los 500 kg")
    private Double peso;

    @DecimalMin(value = "0.0", message = "La altura no puede ser negativa")
    @DecimalMax(value = "300.0", message = "La altura no puede superar los 300 cm")
    private Double altura;

    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 150, message = "La edad no puede superar los 150 años")
    private Integer edad;

    // Objetivos y actividad
    private List<String> objetivos;

    @Min(value = 0, message = "La frecuencia semanal no puede ser negativa")
    @Max(value = 7, message = "La frecuencia semanal no puede superar los 7 días")
    private Integer frecuenciaSemanal;

    private List<String> tipoActividad;

    // Preferencias alimenticias
    private List<String> tipoDieta;
    private List<String> alergias;
    private List<String> gustos;

    @Min(value = 1, message = "Las comidas al día deben ser al menos 1")
    @Max(value = 10, message = "Las comidas al día no pueden superar 10")
    private Integer comidasAlDia;
}
