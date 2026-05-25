package com.neup.web.controller;

import com.neup.web.dto.ProfileDTO;
import com.neup.web.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Persona", description = "Operaciones con los datos de persona")
@RequestMapping("/api/persona")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfileController {
        private final ProfileService perfilService;

        @Operation(summary = "Obtener perfil persona")
        @GetMapping("/{usuarioId}")
        public ResponseEntity<ProfileDTO> obtenerPerfil(@PathVariable String usuarioId) {
            return ResponseEntity.ok(perfilService.obtenerPerfil(usuarioId));
        }

        @Operation(summary = "Modificar perfil persona")
        @PutMapping("/{usuarioId}")
        public ResponseEntity<ProfileDTO> guardarPerfil(
                @PathVariable String usuarioId,
                @RequestBody ProfileDTO dto) {
            return ResponseEntity.ok(perfilService.guardarPerfil(usuarioId, dto));
        }
}
