package com.neup.web.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.neup.web.integration.ConnectionFactory;
import com.neup.web.model.Usuario;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.neup.web.utils.ConstantesNumericas.CERO;
import static com.neup.web.utils.repository.ConstantesPersonaRepository.*;

@Repository
public class UsuarioRepository {

    private static final String COLECCION = "usuario";
    private static final String PASSWORD_TEMPORAL = "passwordTemporal";

    private MongoCollection<Document> getColeccion() {
        MongoDatabase db = ConnectionFactory.getDatabase();
        return db.getCollection(COLECCION);
    }

    // Buscar por nombre de usuario
    public Optional<Document> findByUsuario(String usuario) {
        Document filtro = new Document(USUARIO, usuario);
        Document resultado = getColeccion().find(filtro).first();
        return Optional.ofNullable(resultado);
    }

    // Buscar por email
    public Optional<Document> findByEmail(String email) {
        Document filtro = new Document(EMAIL, email);
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
                .append(USUARIO, usuario.getUsuario())
                .append(EMAIL, usuario.getEmail())
                .append(PASSWORD, usuario.getPassword())
                .append(PASSWORD_TEMPORAL, false); // Por defecto no es temporal

        getColeccion().insertOne(doc);
        return doc.getObjectId("_id");
    }

    // Verificar si existe usuario o email
    public boolean existeUsuarioOEmail(String usuario, String email) {
        Document filtro = new Document("$or", java.util.List.of(
                new Document(USUARIO, usuario),
                new Document(EMAIL, email)
        ));
        return getColeccion().find(filtro).first() != null;
    }

    // Actualizar password sin cambiar el estado temporal
    public boolean actualizarPassword(String id, String nuevaPassword) {
        try {
            Document filtro = new Document("_id", new ObjectId(id));
            Document update = new Document("$set", new Document(PASSWORD, nuevaPassword));
            return getColeccion().updateOne(filtro, update).getModifiedCount() > CERO;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // Actualizar password y marcar como temporal o no
    public boolean actualizarPasswordTemporal(String id, String nuevaPassword, boolean esTemporal) {
        try {
            Document filtro = new Document("_id", new ObjectId(id));
            Document update = new Document("$set", new Document()
                    .append(PASSWORD, nuevaPassword)
                    .append(PASSWORD_TEMPORAL, esTemporal)
            );
            return getColeccion().updateOne(filtro, update).getModifiedCount() > CERO;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // Actualizar password por nombre de usuario y marcar como temporal
    public boolean actualizarPasswordTemporalPorUsuario(String usuario, String nuevaPassword, boolean esTemporal) {
        try {
            Document filtro = new Document(USUARIO, usuario);
            Document update = new Document("$set", new Document()
                    .append(PASSWORD, nuevaPassword)
                    .append(PASSWORD_TEMPORAL, esTemporal)
            );
            return getColeccion().updateOne(filtro, update).getModifiedCount() > CERO;
        } catch (Exception e) {
            return false;
        }
    }

    // Eliminar usuario
    public boolean eliminar(String id) {
        try {
            Document filtro = new Document("_id", new ObjectId(id));
            return getColeccion().deleteOne(filtro).getDeletedCount() > CERO;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

