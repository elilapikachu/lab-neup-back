package com.neup.web.service;

import com.neup.web.dto.RecetaDTO;
import com.neup.web.model.Receta;
import com.neup.web.repository.PersonaRepository;
import com.neup.web.repository.RecetaRepository;
import com.neup.web.utils.repository.ConstantesRecetaRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RecetaService {

    private final RecetaRepository recetaRepository;
    private final DocumentoService documentoService;
    private final PersonaRepository personaRepository;

    public RecetaService(RecetaRepository recetaRepository, DocumentoService documentoService,
                         PersonaRepository personaRepository) {
        this.recetaRepository  = recetaRepository;
        this.documentoService  = documentoService;
        this.personaRepository = personaRepository;
    }

    // ── Crear ─────────────────────────────────────────────────────────────────

    public RecetaDTO.RecetaIdResponse crear(RecetaDTO.RecetaRequest request) {
        Receta receta = mapearRequest(request);
        ObjectId id = recetaRepository.insertar(receta);
        return RecetaDTO.RecetaIdResponse.builder()
                .id(id.toHexString())
                .mensaje("Receta creada correctamente")
                .build();
    }

    // ── Obtener ───────────────────────────────────────────────────────────────

    public Optional<RecetaDTO.RecetaResponse> obtenerPorId(String id) {
        return recetaRepository.findById(id).map(this::mapearResponse);
    }

    public List<RecetaDTO.RecetaResponse> obtenerTodas() {
        return recetaRepository.findAll().stream()
                .map(this::mapearResponse)
                .toList();
    }

    public List<RecetaDTO.RecetaResponse> obtenerPublicas() {
        return recetaRepository.findByVisibilidad(ConstantesRecetaRepository.VISIBILIDAD_PUBLICA)
                .stream().map(this::mapearResponse).toList();
    }

    public List<RecetaDTO.RecetaResponse> obtenerPorPersona(String personaId) {
        return recetaRepository.findByPersonaId(personaId).stream()
                .map(this::mapearResponse)
                .toList();
    }

    // ── Actualizar ────────────────────────────────────────────────────────────

    public boolean actualizar(String id, RecetaDTO.RecetaRequest request) {
        Document campos = new Document();

        if (request.getNombreReceta()     != null) campos.append(ConstantesRecetaRepository.CAMPO_NOMBRE_RECETA,     request.getNombreReceta());
        if (request.getTiempoPreparacion()!= null) campos.append(ConstantesRecetaRepository.CAMPO_TIEMPO_PREPARACION, request.getTiempoPreparacion());
        if (request.getTags()             != null) campos.append(ConstantesRecetaRepository.CAMPO_TAGS,               request.getTags());
        if (request.getVisibilidad()      != null) campos.append(ConstantesRecetaRepository.CAMPO_VISIBILIDAD,        request.getVisibilidad());

        if (request.getNutricion() != null) {
            Document nutricion = new Document()
                    .append(ConstantesRecetaRepository.CAMPO_KCAL,         request.getNutricion().getKcal())
                    .append(ConstantesRecetaRepository.CAMPO_PROTEINAS,    request.getNutricion().getProteinas())
                    .append(ConstantesRecetaRepository.CAMPO_CARBOHIDRATOS,request.getNutricion().getCarbohidratos())
                    .append(ConstantesRecetaRepository.CAMPO_FIBRA,        request.getNutricion().getFibra())
                    .append(ConstantesRecetaRepository.CAMPO_VITAMINAS,    request.getNutricion().getVitaminas())
                    .append(ConstantesRecetaRepository.CAMPO_MINERALES,    request.getNutricion().getMinerales());
            campos.append(ConstantesRecetaRepository.CAMPO_NUTRICION, nutricion);
        }

        if (request.getIngredientes() != null) {
            List<Document> ingredientesDoc = request.getIngredientes().stream()
                    .map(ing -> new Document()
                            .append(ConstantesRecetaRepository.CAMPO_INGREDIENTE_ID,     ing.getIngredienteId())
                            .append(ConstantesRecetaRepository.CAMPO_NOMBRE_INGREDIENTE, ing.getNombreIngrediente())
                            .append(ConstantesRecetaRepository.CAMPO_CANTIDAD,           ing.getCantidad())
                            .append(ConstantesRecetaRepository.CAMPO_TIPO_INGREDIENTE,   ing.getTipoIngrediente())
                            .append(ConstantesRecetaRepository.CAMPO_TIPO_CANTIDAD,      ing.getTipoCantidad()))
                    .toList();
            campos.append(ConstantesRecetaRepository.CAMPO_INGREDIENTES, ingredientesDoc);
        }

        if (campos.isEmpty()) return false;
        return recetaRepository.actualizar(id, campos);
    }

    // ── Imágenes ──────────────────────────────────────────────────────────────

    public RecetaDTO.RecetaIdResponse agregarImagen(String recetaId, MultipartFile archivo, String nombre) throws IOException {
        var docResponse = documentoService.guardarImagen(archivo, nombre);
        ObjectId imagenId = new ObjectId(docResponse.getId());
        recetaRepository.agregarImagen(recetaId, imagenId);
        return RecetaDTO.RecetaIdResponse.builder()
                .id(docResponse.getId())
                .mensaje("Imagen agregada a la receta correctamente")
                .build();
    }

    public boolean eliminarImagen(String recetaId, String imagenId) {
        ObjectId oid = new ObjectId(imagenId);
        boolean eliminadoReceta = recetaRepository.eliminarImagen(recetaId, oid);
        if (eliminadoReceta) documentoService.eliminar(imagenId);
        return eliminadoReceta;
    }

    // ── Recomendadas ──────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    public RecetaDTO.RecomendadasResponse obtenerRecomendadas(String personaId) {
        Document personaDoc = personaRepository.findById(personaId).orElse(null);

        List<String> gustos    = List.of();
        List<String> alergias  = List.of();
        List<String> objetivos = List.of();

        if (personaDoc != null) {
            Document prefs = (Document) personaDoc.get("preferencias");
            if (prefs != null) {
                gustos    = safeStringList(prefs, "gustos");
                alergias  = safeStringList(prefs, "alergias");
                objetivos = safeStringList(prefs, "objetivos");
            }
        }

        boolean tienePreferencias = !gustos.isEmpty() || !alergias.isEmpty() || !objetivos.isEmpty();

        if (!tienePreferencias) {
            return RecetaDTO.RecomendadasResponse.builder()
                    .tienePreferencias(false)
                    .recetas(List.of())
                    .build();
        }

        final List<String> g = gustos;
        final List<String> a = alergias;
        final List<String> o = objetivos;

        record Scored(int score, RecetaDTO.RecetaResponse receta) {}

        List<RecetaDTO.RecetaResponse> recomendadas = recetaRepository
                .findByVisibilidad(ConstantesRecetaRepository.VISIBILIDAD_PUBLICA)
                .stream()
                .map(doc -> new Scored(puntajeReceta(doc, g, a, o), mapearResponse(doc)))
                .filter(s -> s.score() > Integer.MIN_VALUE)
                .sorted(Comparator.comparingInt(Scored::score).reversed())
                .map(Scored::receta)
                .limit(6)
                .toList();

        return RecetaDTO.RecomendadasResponse.builder()
                .tienePreferencias(true)
                .recetas(recomendadas)
                .build();
    }

    @SuppressWarnings("unchecked")
    private int puntajeReceta(Document doc, List<String> gustos, List<String> alergias, List<String> objetivos) {
        int score = 0;

        List<Document> ingDocs = (List<Document>) doc.get(ConstantesRecetaRepository.CAMPO_INGREDIENTES);
        if (ingDocs != null) {
            for (Document ing : ingDocs) {
                String nombre = ing.getString(ConstantesRecetaRepository.CAMPO_NOMBRE_INGREDIENTE);
                if (nombre == null) continue;
                String nombreLower = nombre.toLowerCase();

                for (String alergia : alergias) {
                    if (nombreLower.contains(alergia.toLowerCase())) return Integer.MIN_VALUE;
                }
                for (String gusto : gustos) {
                    if (nombreLower.contains(gusto.toLowerCase())) score += 2;
                }
            }
        }

        Document nutDoc = (Document) doc.get(ConstantesRecetaRepository.CAMPO_NUTRICION);
        if (nutDoc != null) {
            double kcal      = getAsDouble(nutDoc, ConstantesRecetaRepository.CAMPO_KCAL);
            double proteinas = getAsDouble(nutDoc, ConstantesRecetaRepository.CAMPO_PROTEINAS);
            for (String obj : objetivos) {
                switch (obj.toLowerCase()) {
                    case "muscular"          -> { if (proteinas >= 20) score += 2; }
                    case "bajar"             -> { if (kcal > 0 && kcal <= 400) score += 2; }
                    case "subir", "energia", "energía" -> { if (kcal >= 500) score += 1; }
                    case "salud"             -> score += 1;
                    default                  -> {}
                }
            }
        }

        return score;
    }

    @SuppressWarnings("unchecked")
    private static List<String> safeStringList(Document doc, String key) {
        List<String> list = (List<String>) doc.get(key);
        return list != null ? list : List.of();
    }

    // ── Eliminar ──────────────────────────────────────────────────────────────

    public boolean eliminar(String id) {
        return recetaRepository.eliminar(id);
    }

    // ── Mappers ───────────────────────────────────────────────────────────────

    private Receta mapearRequest(RecetaDTO.RecetaRequest request) {
        List<Receta.Ingrediente> ingredientes = new ArrayList<>();
        if (request.getIngredientes() != null) {
            for (var ing : request.getIngredientes()) {
                ingredientes.add(Receta.Ingrediente.builder()
                        .ingredienteId(ing.getIngredienteId())
                        .nombreIngrediente(ing.getNombreIngrediente())
                        .cantidad(ing.getCantidad())
                        .tipoIngrediente(ing.getTipoIngrediente())
                        .tipoCantidad(ing.getTipoCantidad())
                        .build());
            }
        }

        Receta.Nutricion nutricion = null;
        if (request.getNutricion() != null) {
            nutricion = Receta.Nutricion.builder()
                    .kcal(request.getNutricion().getKcal())
                    .proteinas(request.getNutricion().getProteinas())
                    .carbohidratos(request.getNutricion().getCarbohidratos())
                    .fibra(request.getNutricion().getFibra())
                    .vitaminas(request.getNutricion().getVitaminas())
                    .minerales(request.getNutricion().getMinerales())
                    .build();
        }

        return Receta.builder()
                .nombreReceta(request.getNombreReceta())
                .ingredientes(ingredientes)
                .nutricion(nutricion)
                .tags(request.getTags())
                .tiempoPreparacion(request.getTiempoPreparacion())
                .creadaPor(request.getCreadaPor() != null ? new ObjectId(request.getCreadaPor()) : null)
                .esPersonalizada(request.isEsPersonalizada())
                .visibilidad(request.getVisibilidad())
                .build();
    }

    /** Lee un campo numérico de un Document tolerando Int32, Int64 y Double. */
    private static double getAsDouble(Document doc, String key) {
        Object val = doc.get(key);
        if (val == null) return 0.0;
        return ((Number) val).doubleValue();
    }

    /** Lee un campo como String tolerando ObjectId y otros tipos. */
    private static String getAsString(Document doc, String key) {
        Object val = doc.get(key);
        if (val == null) return null;
        if (val instanceof ObjectId oid) return oid.toHexString();
        return val.toString();
    }

    @SuppressWarnings("unchecked")
    private RecetaDTO.RecetaResponse mapearResponse(Document doc) {
        // Ingredientes
        List<RecetaDTO.IngredienteResponse> ingredientes = new ArrayList<>();
        List<Document> ingDocs = (List<Document>) doc.get(ConstantesRecetaRepository.CAMPO_INGREDIENTES);
        if (ingDocs != null) {
            for (Document ing : ingDocs) {
                ingredientes.add(RecetaDTO.IngredienteResponse.builder()
                        .ingredienteId(getAsString(ing, ConstantesRecetaRepository.CAMPO_INGREDIENTE_ID))
                        .nombreIngrediente(getAsString(ing, ConstantesRecetaRepository.CAMPO_NOMBRE_INGREDIENTE))
                        .cantidad(getAsDouble(ing, ConstantesRecetaRepository.CAMPO_CANTIDAD))
                        .tipoIngrediente(getAsString(ing, ConstantesRecetaRepository.CAMPO_TIPO_INGREDIENTE))
                        .tipoCantidad(getAsString(ing, ConstantesRecetaRepository.CAMPO_TIPO_CANTIDAD))
                        .build());
            }
        }

        // Nutrición
        RecetaDTO.NutricionResponse nutricion = null;
        Document nutDoc = (Document) doc.get(ConstantesRecetaRepository.CAMPO_NUTRICION);
        if (nutDoc != null) {
            nutricion = RecetaDTO.NutricionResponse.builder()
                    .kcal(getAsDouble(nutDoc, ConstantesRecetaRepository.CAMPO_KCAL))
                    .proteinas(getAsDouble(nutDoc, ConstantesRecetaRepository.CAMPO_PROTEINAS))
                    .carbohidratos(getAsDouble(nutDoc, ConstantesRecetaRepository.CAMPO_CARBOHIDRATOS))
                    .fibra(getAsDouble(nutDoc, ConstantesRecetaRepository.CAMPO_FIBRA))
                    .vitaminas((List<String>) nutDoc.get(ConstantesRecetaRepository.CAMPO_VITAMINAS))
                    .minerales((List<String>) nutDoc.get(ConstantesRecetaRepository.CAMPO_MINERALES))
                    .build();
        }

        // Imágenes
        List<String> imagenes = new ArrayList<>();
        List<?> imgList = (List<?>) doc.get(ConstantesRecetaRepository.CAMPO_IMAGEN);
        if (imgList != null) {
            for (Object obj : imgList) {
                if (obj instanceof ObjectId oid) imagenes.add(oid.toHexString());
            }
        }

        ObjectId creadaPor = doc.getObjectId(ConstantesRecetaRepository.CAMPO_CREADA_POR);

        return RecetaDTO.RecetaResponse.builder()
                .id(getAsString(doc, ConstantesRecetaRepository.CAMPO_ID))
                .nombreReceta(getAsString(doc, ConstantesRecetaRepository.CAMPO_NOMBRE_RECETA))
                .ingredientes(ingredientes)
                .nutricion(nutricion)
                .tags((List<String>) doc.get(ConstantesRecetaRepository.CAMPO_TAGS))
                .tiempoPreparacion(getAsString(doc, ConstantesRecetaRepository.CAMPO_TIEMPO_PREPARACION))
                .creadaPor(creadaPor != null ? creadaPor.toHexString() : null)
                .esPersonalizada(Boolean.TRUE.equals(doc.getBoolean(ConstantesRecetaRepository.CAMPO_ES_PERSONALIZADA)))
                .visibilidad(getAsString(doc, ConstantesRecetaRepository.CAMPO_VISIBILIDAD))
                .imagen(imagenes)
                .build();
    }
}
