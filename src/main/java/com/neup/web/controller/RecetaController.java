package com.neup.web.controller;

import com.neup.web.dto.RecetaDTO;
import com.neup.web.service.RecetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/recetas")
@Tag(name = "Recetas", description = "Gestión de recetas")
public class RecetaController {

    private final RecetaService recetaService;

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    // ── POST /api/recetas ────────────────────────────────────────────────────
    @Operation(summary = "Crear receta")
    @PostMapping
    public ResponseEntity<RecetaDTO.RecetaIdResponse> crear(@Valid @RequestBody RecetaDTO.RecetaRequest request) {
        return ResponseEntity.ok(recetaService.crear(request));
    }

    // ── GET /api/recetas ─────────────────────────────────────────────────────
    @Operation(summary = "Obtener todas las recetas")
    @GetMapping
    public ResponseEntity<List<RecetaDTO.RecetaResponse>> obtenerTodas() {
        return ResponseEntity.ok(recetaService.obtenerTodas());
    }

    // ── GET /api/recetas/publicas ─────────────────────────────────────────────
    @Operation(summary = "Obtener recetas públicas")
    @GetMapping("/publicas")
    public ResponseEntity<List<RecetaDTO.RecetaResponse>> obtenerPublicas() {
        return ResponseEntity.ok(recetaService.obtenerPublicas());
    }

    // ── GET /api/recetas/{id} ─────────────────────────────────────────────────
    @Operation(summary = "Obtener receta por id")
    @GetMapping("/{id}")
    public ResponseEntity<RecetaDTO.RecetaResponse> obtenerPorId(@PathVariable String id) {
        return recetaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── GET /api/recetas/persona/{personaId} ──────────────────────────────────
    @Operation(summary = "Obtener recetas de una persona")
    @GetMapping("/persona/{personaId}")
    public ResponseEntity<List<RecetaDTO.RecetaResponse>> obtenerPorPersona(@PathVariable String personaId) {
        return ResponseEntity.ok(recetaService.obtenerPorPersona(personaId));
    }

    // ── PUT /api/recetas/{id} ─────────────────────────────────────────────────
    @Operation(summary = "Actualizar receta")
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(@PathVariable String id,
                                           @Valid @RequestBody RecetaDTO.RecetaRequest request) {
        boolean actualizado = recetaService.actualizar(id, request);
        return actualizado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ── POST /api/recetas/{id}/imagenes ───────────────────────────────────────
    @Operation(summary = "Agregar imagen a una receta")
    @PostMapping(value = "/{id}/imagenes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecetaDTO.RecetaIdResponse> agregarImagen(
            @PathVariable String id,
            @RequestPart("archivo") MultipartFile archivo,
            @RequestPart(value = "nombre", required = false) String nombre) throws IOException {

        String nombreFinal = (nombre != null && !nombre.isBlank()) ? nombre : archivo.getOriginalFilename();
        return ResponseEntity.ok(recetaService.agregarImagen(id, archivo, nombreFinal));
    }

    // ── DELETE /api/recetas/{id}/imagenes/{imagenId} ──────────────────────────
    @Operation(summary = "Eliminar imagen de una receta")
    @DeleteMapping("/{id}/imagenes/{imagenId}")
    public ResponseEntity<Void> eliminarImagen(@PathVariable String id,
                                               @PathVariable String imagenId) {
        boolean eliminado = recetaService.eliminarImagen(id, imagenId);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ── DELETE /api/recetas/{id} ──────────────────────────────────────────────
    @Operation(summary = "Eliminar receta")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        boolean eliminado = recetaService.eliminar(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
