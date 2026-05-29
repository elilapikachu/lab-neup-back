package com.neup.web.controller;

import com.neup.web.dto.DietaDTO;
import com.neup.web.service.DietaService;
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
@RequestMapping("/api/dietas")
@Tag(name = "Dietas", description = "Gestión de dietas")
public class DietaController {

    private final DietaService dietaService;

    public DietaController(DietaService dietaService) {
        this.dietaService = dietaService;
    }

    // ── POST /api/dietas ──────────────────────────────────────────────────────
    @Operation(summary = "Crear dieta")
    @PostMapping
    public ResponseEntity<DietaDTO.DietaIdResponse> crear(@Valid @RequestBody DietaDTO.DietaRequest request) {
        return ResponseEntity.ok(dietaService.crear(request));
    }

    // ── GET /api/dietas ───────────────────────────────────────────────────────
    @Operation(summary = "Obtener todas las dietas")
    @GetMapping
    public ResponseEntity<List<DietaDTO.DietaResponse>> obtenerTodas() {
        return ResponseEntity.ok(dietaService.obtenerTodas());
    }

    // ── GET /api/dietas/publicas ──────────────────────────────────────────────
    @Operation(summary = "Obtener dietas públicas")
    @GetMapping("/publicas")
    public ResponseEntity<List<DietaDTO.DietaResponse>> obtenerPublicas() {
        return ResponseEntity.ok(dietaService.obtenerPublicas());
    }

    // ── GET /api/dietas/persona/{personaId} ───────────────────────────────────
    @Operation(summary = "Obtener dietas creadas por una persona")
    @GetMapping("/persona/{personaId}")
    public ResponseEntity<List<DietaDTO.DietaResponse>> obtenerPorPersona(@PathVariable String personaId) {
        return ResponseEntity.ok(dietaService.obtenerPorPersona(personaId));
    }

    // ── GET /api/dietas/recomendadas/{personaId} ──────────────────────────────
    @Operation(summary = "Obtener dietas recomendadas según preferencias del usuario")
    @GetMapping("/recomendadas/{personaId}")
    public ResponseEntity<DietaDTO.RecomendadasResponse> obtenerRecomendadas(@PathVariable String personaId) {
        return ResponseEntity.ok(dietaService.obtenerRecomendadas(personaId));
    }

    // ── GET /api/dietas/{id} ──────────────────────────────────────────────────
    @Operation(summary = "Obtener dieta por id")
    @GetMapping("/{id}")
    public ResponseEntity<DietaDTO.DietaResponse> obtenerPorId(@PathVariable String id) {
        return dietaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── PUT /api/dietas/{id} ──────────────────────────────────────────────────
    @Operation(summary = "Actualizar dieta")
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(@PathVariable String id,
                                           @Valid @RequestBody DietaDTO.DietaRequest request) {
        boolean actualizado = dietaService.actualizar(id, request);
        return actualizado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ── POST /api/dietas/{id}/plan ────────────────────────────────────────────
    @Operation(summary = "Agregar receta al plan semanal de la dieta")
    @PostMapping("/{id}/plan")
    public ResponseEntity<Void> agregarRecetaAlPlan(
            @PathVariable String id,
            @Valid @RequestBody DietaDTO.AgregarRecetaPlanRequest request) {
        boolean ok = dietaService.agregarRecetaAlPlan(id, request);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ── DELETE /api/dietas/{id}/plan/{recetaId} ───────────────────────────────
    @Operation(summary = "Eliminar receta del plan semanal")
    @DeleteMapping("/{id}/plan/{recetaId}")
    public ResponseEntity<Void> eliminarRecetaDelPlan(@PathVariable String id,
                                                      @PathVariable String recetaId) {
        boolean ok = dietaService.eliminarRecetaDelPlan(id, recetaId);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ── POST /api/dietas/{id}/portada ─────────────────────────────────────────
    @Operation(summary = "Subir o reemplazar la portada de la dieta")
    @PostMapping(value = "/{id}/portada", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DietaDTO.DietaIdResponse> subirPortada(
            @PathVariable String id,
            @RequestPart("archivo") MultipartFile archivo,
            @RequestPart(value = "nombre", required = false) String nombre) throws IOException {

        String nombreFinal = (nombre != null && !nombre.isBlank()) ? nombre : archivo.getOriginalFilename();
        return ResponseEntity.ok(dietaService.subirPortada(id, archivo, nombreFinal));
    }

    // ── DELETE /api/dietas/{id} ───────────────────────────────────────────────
    @Operation(summary = "Eliminar dieta")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        boolean eliminado = dietaService.eliminar(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
