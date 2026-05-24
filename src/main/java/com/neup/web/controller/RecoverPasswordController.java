package com.neup.web.controller;

import com.neup.web.dto.RecoverPasswordDTO;
import com.neup.web.service.RecoverPasswordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Recuperación de contraseña")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RecoverPasswordController {

    private final RecoverPasswordService recuperarPasswordService;

    @PostMapping("/recuperar-password")
    public ResponseEntity<String> recuperarPassword(@RequestBody RecoverPasswordDTO dto) {
        try {
            recuperarPasswordService.recuperarPassword(dto.getUsuario());
            return ResponseEntity.ok("Correo de recuperación enviado correctamente");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
