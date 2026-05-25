package com.neup.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// EnviarEmailDTO.java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnviarEmailDTO {
    private String to;
    private String subject;
    private String body;
}
