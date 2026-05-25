package com.neup.web.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.neup.web.integration.ConnectionFactory;
import com.neup.web.model.Dieta;
import com.neup.web.utils.repository.ConstantesDietaRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DietaRepository {

    private MongoCollection<Document> getColeccion() {
        MongoDatabase db = ConnectionFactory.getDatabase();
        return db.getCollection(ConstantesDietaRepository.COLECCION);
    }

    public Optional<Document> findById(String id) {
        Document filtro = new Document(ConstantesDietaRepository.CAMPO_ID, new ObjectId(id));
        return Optional.ofNullable(getColeccion().find(filtro).first());
    }

    public List<Document> findAll() {
        return getColeccion().find().into(new ArrayList<>());
    }

    public List<Document> findByVisibilidad(String visibilidad) {
        Document filtro = new Document(ConstantesDietaRepository.CAMPO_VISIBILIDAD, visibilidad);
        return getColeccion().find(filtro).into(new ArrayList<>());
    }

    public ObjectId insertar(Dieta dieta) {
        List<Document> planDoc = new ArrayList<>();
        if (dieta.getPlanSemanal() != null) {
            for (Dieta.PlanSemanal plan : dieta.getPlanSemanal()) {
                planDoc.add(new Document()
                        .append(ConstantesDietaRepository.CAMPO_RECETA_ID,   plan.getRecetaId())
                        .append(ConstantesDietaRepository.CAMPO_TIPO_COMIDA, plan.getTipoComida()));
            }
        }

        Document doc = new Document()
                .append(ConstantesDietaRepository.CAMPO_NOMBRE_DIETA,     dieta.getNombreDieta())
                .append(ConstantesDietaRepository.CAMPO_DESCRIPCION,       dieta.getDescripcion())
                .append(ConstantesDietaRepository.CAMPO_METAS,             dieta.getMetas() != null ? dieta.getMetas() : List.of())
                .append(ConstantesDietaRepository.CAMPO_PLAN_SEMANAL,      planDoc)
                .append(ConstantesDietaRepository.CAMPO_ES_PERSONALIZADA,  dieta.isEsPersonalizada())
                .append(ConstantesDietaRepository.CAMPO_VISIBILIDAD,       dieta.getVisibilidad())
                .append(ConstantesDietaRepository.CAMPO_PORTADA,           null);

        getColeccion().insertOne(doc);
        return doc.getObjectId(ConstantesDietaRepository.CAMPO_ID);
    }

    public boolean actualizar(String id, Document campos) {
        Document filtro = new Document(ConstantesDietaRepository.CAMPO_ID, new ObjectId(id));
        Document update = new Document("$set", campos);
        return getColeccion().updateOne(filtro, update).getModifiedCount() > 0;
    }

    public boolean actualizarPortada(String dietaId, ObjectId documentoId) {
        Document filtro = new Document(ConstantesDietaRepository.CAMPO_ID, new ObjectId(dietaId));
        Document update = new Document("$set", new Document(ConstantesDietaRepository.CAMPO_PORTADA, documentoId));
        return getColeccion().updateOne(filtro, update).getModifiedCount() > 0;
    }

    public boolean agregarRecetaPlan(String dietaId, ObjectId recetaId, String tipoComida) {
        Document filtro = new Document(ConstantesDietaRepository.CAMPO_ID, new ObjectId(dietaId));
        Document entrada = new Document()
                .append(ConstantesDietaRepository.CAMPO_RECETA_ID,   recetaId)
                .append(ConstantesDietaRepository.CAMPO_TIPO_COMIDA, tipoComida);
        Document update = new Document("$push", new Document(ConstantesDietaRepository.CAMPO_PLAN_SEMANAL, entrada));
        return getColeccion().updateOne(filtro, update).getModifiedCount() > 0;
    }

    public boolean eliminarRecetaPlan(String dietaId, ObjectId recetaId) {
        Document filtro = new Document(ConstantesDietaRepository.CAMPO_ID, new ObjectId(dietaId));
        Document pull   = new Document(ConstantesDietaRepository.CAMPO_RECETA_ID, recetaId);
        Document update = new Document("$pull", new Document(ConstantesDietaRepository.CAMPO_PLAN_SEMANAL, pull));
        return getColeccion().updateOne(filtro, update).getModifiedCount() > 0;
    }

    public boolean eliminar(String id) {
        Document filtro = new Document(ConstantesDietaRepository.CAMPO_ID, new ObjectId(id));
        return getColeccion().deleteOne(filtro).getDeletedCount() > 0;
    }
}
