package com.neup.web.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.neup.web.integration.ConnectionFactory;
import com.neup.web.model.Persona;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PersonaRepository {


    private static final String COLECCION = "persona";

    private MongoCollection<Document> getColeccion() {
        MongoDatabase db = ConnectionFactory.getDatabase();
        return db.getCollection(COLECCION);
    }

    public Optional<Document> findByUsuarioId(String usuarioId) {
        Document filtro = new Document("usuario_id", new org.bson.types.ObjectId(usuarioId));
        Document resultado = getColeccion().find(filtro).first();
        return Optional.ofNullable(resultado);
    }

    public Optional<Document> findById(String id) {
        Document filtro = new Document("_id", new org.bson.types.ObjectId(id));
        Document resultado = getColeccion().find(filtro).first();
        return Optional.ofNullable(resultado);
    }
    public ObjectId insertar(Persona persona) {
        Document contactos = new Document()
                .append("telefono", persona.getContactos() != null ? persona.getContactos().getTelefono() : null)
                .append("otro_email", persona.getContactos() != null ? persona.getContactos().getOtroEmail() : null);

        Document caracteristicas = new Document()
                .append("peso", persona.getCaracteristicasFisicas() != null ? persona.getCaracteristicasFisicas().getPeso() : null)
                .append("altura", persona.getCaracteristicasFisicas() != null ? persona.getCaracteristicasFisicas().getAltura() : null);

        Document preferencias = new Document()
                .append("gustos", List.of())
                .append("alergias", List.of())
                .append("tipo_dieta", List.of())
                .append("objetivos", List.of());

        Document dietas = new Document()
                .append("guardadas", List.of())
                .append("personalizadas", List.of());

        Document recetas = new Document()
                .append("guardadas", List.of())
                .append("personalizadas", List.of());

        Document doc = new Document()
                .append("nombres", persona.getNombres())
                .append("apellidos", persona.getApellidos())
                .append("contactos", contactos)
                .append("caracteristicas_fisicas", caracteristicas)
                .append("preferencias", preferencias)
                .append("usuario_id", persona.getUsuarioId())
                .append("dietas", dietas)
                .append("recetas", recetas);

        getColeccion().insertOne(doc);
        return doc.getObjectId("_id");
    }

    public boolean actualizar(String id, Document campos) {
        Document filtro = new Document("_id", new ObjectId(id));
        Document update = new Document("$set", campos);
        return getColeccion().updateOne(filtro, update).getModifiedCount() > 0;
    }

    public boolean eliminar(String id) {
        Document filtro = new Document("_id", new ObjectId(id));
        return getColeccion().deleteOne(filtro).getDeletedCount() > 0;
    }

    // ── Guardadas: recetas ────────────────────────────────────────────────────

    public void addRecetaGuardada(String personaId, String recetaId) {
        Document filtro = new Document("_id", new ObjectId(personaId));
        Document update = new Document("$addToSet", new Document("recetas.guardadas", recetaId));
        getColeccion().updateOne(filtro, update);
    }

    public void removeRecetaGuardada(String personaId, String recetaId) {
        Document filtro = new Document("_id", new ObjectId(personaId));
        Document update = new Document("$pull", new Document("recetas.guardadas", recetaId));
        getColeccion().updateOne(filtro, update);
    }

    @SuppressWarnings("unchecked")
    public List<String> getRecetasGuardadas(String personaId) {
        Document doc = getColeccion().find(new Document("_id", new ObjectId(personaId))).first();
        if (doc == null) return List.of();
        Document recetas = (Document) doc.get("recetas");
        if (recetas == null) return List.of();
        List<String> guardadas = (List<String>) recetas.get("guardadas");
        return guardadas != null ? guardadas : List.of();
    }

    // ── Guardadas: dietas ─────────────────────────────────────────────────────

    public void addDietaGuardada(String personaId, String dietaId) {
        Document filtro = new Document("_id", new ObjectId(personaId));
        Document update = new Document("$addToSet", new Document("dietas.guardadas", dietaId));
        getColeccion().updateOne(filtro, update);
    }

    public void removeDietaGuardada(String personaId, String dietaId) {
        Document filtro = new Document("_id", new ObjectId(personaId));
        Document update = new Document("$pull", new Document("dietas.guardadas", dietaId));
        getColeccion().updateOne(filtro, update);
    }

    @SuppressWarnings("unchecked")
    public List<String> getDietasGuardadas(String personaId) {
        Document doc = getColeccion().find(new Document("_id", new ObjectId(personaId))).first();
        if (doc == null) return List.of();
        Document dietas = (Document) doc.get("dietas");
        if (dietas == null) return List.of();
        List<String> guardadas = (List<String>) dietas.get("guardadas");
        return guardadas != null ? guardadas : List.of();
    }
}
