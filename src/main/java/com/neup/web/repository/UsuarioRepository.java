package com.neup.web.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.neup.web.integration.ConnectionFactory;
import com.neup.web.model.Usuario;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UsuarioRepository {

    private static final String COLECCION = "usuario";

    private MongoCollection<Document> getColeccion() {
        MongoDatabase db = ConnectionFactory.getDatabase();
        return db.getCollection(COLECCION);
    }

    // Buscar por nombre de usuario
    public Optional<Document> findByUsuario(String usuario) {
        Document filtro = new Document("usuario", usuario);
        Document resultado = getColeccion().find(filtro).first();
        return Optional.ofNullable(resultado);
    }

    // Buscar por email
    public Optional<Document> findByEmail(String email) {
        Document filtro = new Document("email", email);
        Document resultado = getColeccion().find(filtro).first();
        return Optional.ofNullable(resultado);
    }

    // Buscar por ID
    public Optional<Document> findById(String id) {
        try {
            Document filtro = new Document("_id", new ObjectId(id));
            Document resultado = getColeccion().find(filtro).first();
            return Optional.ofNullable(resultado);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    // Insertar nuevo usuario
    public ObjectId insertar(Usuario usuario) {
        Document doc = new Document()
                .append("usuario", usuario.getUsuario())
                .append("email", usuario.getEmail())
                .append("password", usuario.getPassword());

        getColeccion().insertOne(doc);
        return doc.getObjectId("_id");
    }

    // Verificar si existe usuario o email
    public boolean existeUsuarioOEmail(String usuario, String email) {
        Document filtro = new Document("$or", java.util.List.of(
                new Document("usuario", usuario),
                new Document("email", email)
        ));
        return getColeccion().find(filtro).first() != null;
    }

    // Actualizar password
    public boolean actualizarPassword(String id, String nuevaPassword) {
        try {
            Document filtro = new Document("_id", new ObjectId(id));
            Document update = new Document("$set", new Document("password", nuevaPassword));
            return getColeccion().updateOne(filtro, update).getModifiedCount() > 0;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // Eliminar usuario
    public boolean eliminar(String id) {
        try {
            Document filtro = new Document("_id", new ObjectId(id));
            return getColeccion().deleteOne(filtro).getDeletedCount() > 0;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
