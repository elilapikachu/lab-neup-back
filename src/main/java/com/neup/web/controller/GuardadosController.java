package com.neup.web.controller;

import com.neup.web.dto.DietaDTO;
import com.neup.web.dto.RecetaDTO;
import com.neup.web.service.GuardadosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/persona")
@Tag(name = "Guardados", description = "Recetas y dietas guardadas por el usuario")
@CrossOrigin(origins = "*")
public class GuardadosController {

    private final GuardadosService guardadosService;

    public GuardadosController(GuardadosService guardadosService) {
        this.guardadosService = guardadosService;
    }

    // ── Recetas ───────────────────────────────────────────────────────────────

    @Operation(summary = "Verificar si una receta está guardada")
    @GetMapping("/{personaId}/recetas/guardadas/{recetaId}/estado")
    public ResponseEntity<Map<String, Boolean>> isRecetaGuardada(
            @PathVariable String personaId,
            @PathVariable String recetaId) {
        boolean guardada = guardadosService.isRecetaGuardada(personaId, recetaId);
        return ResponseEntity.ok(Map.of("guardada", guardada));
    }

    @Operation(summary = "Guardar una receta")
    @PostMapping("/{personaId}/recetas/guardadas/{recetaId}")
    public ResponseEntity<Void> guardarReceta(
            @PathVariable String personaId,
            @PathVariable String recetaId) {
        guardadosService.guardarReceta(personaId, recetaId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Eliminar una receta guardada")
    @DeleteMapping("/{personaId}/recetas/guardadas/{recetaId}")
    public ResponseEntity<Void> desguardarReceta(
            @PathVariable String personaId,
            @PathVariable String recetaId) {
        guardadosService.desguardarReceta(personaId, recetaId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener todas las recetas guardadas")
    @GetMapping("/{personaId}/recetas/guardadas")
    public ResponseEntity<List<RecetaDTO.RecetaResponse>> getRecetasGuardadas(
            @PathVariable String personaId) {
        return ResponseEntity.ok(guardadosService.getRecetasGuardadas(personaId));
    }

    // ── Dietas ────────────────────────────────────────────────────────────────

    @Operation(summary = "Verificar si una dieta está guardada")
    @GetMapping("/{personaId}/dietas/guardadas/{dietaId}/estado")
    public ResponseEntity<Map<String, Boolean>> isDietaGuardada(
            @PathVariable String personaId,
            @PathVariable String dietaId) {
        boolean guardada = guardadosService.isDietaGuardada(personaId, dietaId);
        return ResponseEntity.ok(Map.of("guardada", guardada));
    }

    @Operation(summary = "Guardar una dieta")
    @PostMapping("/{personaId}/dietas/guardadas/{dietaId}")
    public ResponseEntity<Void> guardarDieta(
            @PathVariable String personaId,
            @PathVariable String dietaId) {
        guardadosService.guardarDieta(personaId, dietaId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Eliminar una dieta guardada")
    @DeleteMapping("/{personaId}/dietas/guardadas/{dietaId}")
    public ResponseEntity<Void> desguardarDieta(
            @PathVariable String personaId,
            @PathVariable String dietaId) {
        guardadosService.desguardarDieta(personaId, dietaId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener todas las dietas guardadas")
    @GetMapping("/{personaId}/dietas/guardadas")
    public ResponseEntity<List<DietaDTO.DietaResponse>> getDietasGuardadas(
            @PathVariable String personaId) {
        return ResponseEntity.ok(guardadosService.getDietasGuardadas(personaId));
    }
}
