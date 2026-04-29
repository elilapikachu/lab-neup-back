package com.neup.web.controller;

import com.neup.web.dto.ProfileDTO;
import com.neup.web.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/persona")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfileController {
        private final ProfileService perfilService;

        @GetMapping("/{usuarioId}")
        public ResponseEntity<ProfileDTO> obtenerPerfil(@PathVariable String usuarioId) {
            return ResponseEntity.ok(perfilService.obtenerPerfil(usuarioId));
        }

        @PutMapping("/{usuarioId}")
        public ResponseEntity<ProfileDTO> guardarPerfil(
                @PathVariable String usuarioId,
                @RequestBody ProfileDTO dto) {
            return ResponseEntity.ok(perfilService.guardarPerfil(usuarioId, dto));
        }
}
