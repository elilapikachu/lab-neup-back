package com.neup.web.controller;

import com.neup.web.dto.DocumentoDTO;
import com.neup.web.service.DocumentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/documentos")
@Tag(name = "Documentos", description = "Subida, consulta y eliminación de documentos / imágenes")
public class DocumentoController {

    private final DocumentoService documentoService;

    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    // ── POST /api/documentos ──────────────────────────────────────────────────
    @Operation(summary = "Subir un documento o imagen de forma independiente")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentoDTO.DocumentoResponse> subir(
            @RequestPart("archivo") MultipartFile archivo,
            @RequestPart(value = "nombre", required = false) String nombre) throws IOException {

        String nombreFinal = (nombre != null && !nombre.isBlank()) ? nombre : archivo.getOriginalFilename();
        DocumentoDTO.DocumentoResponse response = documentoService.guardarImagen(archivo, nombreFinal);
        return ResponseEntity.ok(response);
    }

    // ── GET /api/documentos/{id} ──────────────────────────────────────────────
    @Operation(summary = "Obtener información de un documento por id")
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoDTO.DocumentoResponse> obtenerPorId(@PathVariable String id) {
        return documentoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── GET /api/documentos/{id}/archivo ──────────────────────────────────────
    @Operation(summary = "Servir el archivo binario de un documento por id")
    @GetMapping("/{id}/archivo")
    public ResponseEntity<Resource> servirArchivo(@PathVariable String id) {
        var docOpt = documentoService.obtenerPorId(id);
        if (docOpt.isEmpty()) return ResponseEntity.<Resource>notFound().build();

        var doc = docOpt.get();
        try {
            Path rutaArchivo = Paths.get(doc.getRuta());
            Resource resource = new UrlResource(rutaArchivo.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.<Resource>notFound().build();
            }
            String contentType = determinarContentType(doc.getExtension());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + doc.getNombre() + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.<Resource>notFound().build();
        }
    }

    private String determinarContentType(String extension) {
        if (extension == null) return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        return switch (extension.toLowerCase()) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG_VALUE;
            case "png"         -> MediaType.IMAGE_PNG_VALUE;
            case "gif"         -> MediaType.IMAGE_GIF_VALUE;
            case "webp"        -> "image/webp";
            case "pdf"         -> MediaType.APPLICATION_PDF_VALUE;
            default            -> MediaType.APPLICATION_OCTET_STREAM_VALUE;
        };
    }

    // ── DELETE /api/documentos/{id} ───────────────────────────────────────────
    @Operation(summary = "Eliminar un documento por id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        boolean eliminado = documentoService.eliminar(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}