package com.neup.web.controller;

import com.neup.web.dto.DocumentoDTO;
import com.neup.web.service.DocumentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documentos")
@Tag(name = "Documentos", description = "Consulta y eliminación de documentos / imágenes")
public class DocumentoController {

    private final DocumentoService documentoService;

    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    // ── GET /api/documentos/{id} ──────────────────────────────────────────────
    @Operation(summary = "Obtener información de un documento por id")
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoDTO.DocumentoResponse> obtenerPorId(@PathVariable String id) {
        return documentoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── DELETE /api/documentos/{id} ───────────────────────────────────────────
    @Operation(summary = "Eliminar un documento por id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        boolean eliminado = documentoService.eliminar(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
