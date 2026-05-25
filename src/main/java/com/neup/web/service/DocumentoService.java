package com.neup.web.service;

import com.neup.web.dto.DocumentoDTO;
import com.neup.web.repository.DocumentoRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentoService {

    /**
     * Directorio base donde se almacenan las imágenes en disco.
     * Ajústalo según tu entorno (puede venir de application.properties).
     */
    private static final String DIRECTORIO_BASE = "./uploads/imagenes";

    private final DocumentoRepository documentoRepository;

    public DocumentoService(DocumentoRepository documentoRepository) {
        this.documentoRepository = documentoRepository;
    }

    /**
     * Guarda el archivo en disco y registra el documento en MongoDB.
     *
     * @param archivo   MultipartFile recibido desde el controller
     * @param nombre    Nombre descriptivo del documento
     * @return          DocumentoResponse con el id generado
     */
    public DocumentoDTO.DocumentoResponse guardarImagen(MultipartFile archivo, String nombre) throws IOException {
        // Extraer extensión
        String nombreOriginal = archivo.getOriginalFilename() != null
                ? archivo.getOriginalFilename()
                : "archivo";
        String extension = "";
        int punto = nombreOriginal.lastIndexOf('.');
        if (punto >= 0) {
            extension = nombreOriginal.substring(punto + 1).toLowerCase();
        }

        // Crear directorio si no existe
        Path dirPath = Paths.get(DIRECTORIO_BASE);
        Files.createDirectories(dirPath);

        // Nombre único en disco
        String nombreArchivo = UUID.randomUUID() + "." + extension;
        Path rutaArchivo = dirPath.resolve(nombreArchivo);
        Files.write(rutaArchivo, archivo.getBytes());

        // Persistir en MongoDB
        ObjectId id = documentoRepository.insertar(
                nombre,
                archivo.getSize(),
                rutaArchivo.toString(),
                extension
        );

        return DocumentoDTO.DocumentoResponse.builder()
                .id(id.toHexString())
                .nombre(nombre)
                .tamanno(archivo.getSize())
                .ruta(rutaArchivo.toString())
                .extension(extension)
                .mensaje("Imagen guardada correctamente")
                .build();
    }

    /**
     * Obtiene la información de un documento por su id.
     */
    public Optional<DocumentoDTO.DocumentoResponse> obtenerPorId(String id) {
        return documentoRepository.findById(id).map(doc -> DocumentoDTO.DocumentoResponse.builder()
                .id(doc.getObjectId("_id").toHexString())
                .nombre(doc.getString("nombre"))
                .tamanno(doc.getLong("tamanno") != null ? doc.getLong("tamanno") : 0L)
                .ruta(doc.getString("ruta"))
                .extension(doc.getString("extension"))
                .build());
    }

    /**
     * Elimina el documento de MongoDB (sin eliminar el archivo en disco).
     * Si necesitas borrar el archivo también, amplía este método.
     */
    public boolean eliminar(String id) {
        Optional<Document> doc = documentoRepository.findById(id);
        if (doc.isEmpty()) return false;

        // Opcional: borrar archivo del disco
        try {
            String ruta = doc.get().getString("ruta");
            if (ruta != null) {
                Path path = Paths.get(ruta);
                Files.deleteIfExists(path);
            }
        } catch (IOException ignored) {
            // Log pero no bloquea
        }

        return documentoRepository.eliminar(id);
    }
}
