package com.neup.web.controller;

import com.neup.web.dto.IngredienteDTO;
import com.neup.web.service.IngredienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ingredientes")
@Tag(name = "Ingredientes", description = "Catálogo de ingredientes")
public class IngredienteController {

    private final IngredienteService ingredienteService;

    public IngredienteController(IngredienteService ingredienteService) {
        this.ingredienteService = ingredienteService;
    }

    @Operation(summary = "Obtener todos los ingredientes del catálogo")
    @GetMapping
    public ResponseEntity<List<IngredienteDTO.IngredienteResponse>> obtenerTodos() {
        return ResponseEntity.ok(ingredienteService.obtenerTodos());
    }
}
