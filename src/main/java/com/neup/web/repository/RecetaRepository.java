package com.neup.web.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.neup.web.integration.ConnectionFactory;
import com.neup.web.model.Receta;
import com.neup.web.utils.repository.ConstantesRecetaRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RecetaRepository {

    private MongoCollection<Document> getColeccion() {
        MongoDatabase db = ConnectionFactory.getDatabase();
        return db.getCollection(ConstantesRecetaRepository.COLECCION);
    }

    public Optional<Document> findById(String id) {
        Document filtro = new Document(ConstantesRecetaRepository.CAMPO_ID, new ObjectId(id));
        return Optional.ofNullable(getColeccion().find(filtro).first());
    }

    public List<Document> findAll() {
        return getColeccion().find().into(new ArrayList<>());
    }

    public List<Document> findByVisibilidad(String visibilidad) {
        Document filtro = new Document(ConstantesRecetaRepository.CAMPO_VISIBILIDAD, visibilidad);
        return getColeccion().find(filtro).into(new ArrayList<>());
    }

    public List<Document> findByPersonaId(String personaId) {
        Document filtro = new Document(ConstantesRecetaRepository.CAMPO_CREADA_POR, new ObjectId(personaId));
        return getColeccion().find(filtro).into(new ArrayList<>());
    }

    public ObjectId insertar(Receta receta) {
        Document nutricion = new Document()
                .append(ConstantesRecetaRepository.CAMPO_KCAL,          receta.getNutricion() != null ? receta.getNutricion().getKcal() : 0)
                .append(ConstantesRecetaRepository.CAMPO_PROTEINAS,      receta.getNutricion() != null ? receta.getNutricion().getProteinas() : 0)
                .append(ConstantesRecetaRepository.CAMPO_CARBOHIDRATOS,  receta.getNutricion() != null ? receta.getNutricion().getCarbohidratos() : 0)
                .append(ConstantesRecetaRepository.CAMPO_FIBRA,          receta.getNutricion() != null ? receta.getNutricion().getFibra() : 0)
                .append(ConstantesRecetaRepository.CAMPO_VITAMINAS,      receta.getNutricion() != null ? receta.getNutricion().getVitaminas() : List.of())
                .append(ConstantesRecetaRepository.CAMPO_MINERALES,      receta.getNutricion() != null ? receta.getNutricion().getMinerales() : List.of());

        List<Document> ingredientesDoc = new ArrayList<>();
        if (receta.getIngredientes() != null) {
            for (Receta.Ingrediente ing : receta.getIngredientes()) {
                ingredientesDoc.add(new Document()
                        .append(ConstantesRecetaRepository.CAMPO_NOMBRE_INGREDIENTE, ing.getNombreIngrediente())
                        .append(ConstantesRecetaRepository.CAMPO_CANTIDAD,           ing.getCantidad())
                        .append(ConstantesRecetaRepository.CAMPO_TIPO_INGREDIENTE,   ing.getTipoIngrediente()));
            }
        }

        Document doc = new Document()
                .append(ConstantesRecetaRepository.CAMPO_NOMBRE_RECETA,       receta.getNombreReceta())
                .append(ConstantesRecetaRepository.CAMPO_INGREDIENTES,         ingredientesDoc)
                .append(ConstantesRecetaRepository.CAMPO_NUTRICION,            nutricion)
                .append(ConstantesRecetaRepository.CAMPO_TAGS,                 receta.getTags() != null ? receta.getTags() : List.of())
                .append(ConstantesRecetaRepository.CAMPO_TIEMPO_PREPARACION,   receta.getTiempoPreparacion())
                .append(ConstantesRecetaRepository.CAMPO_CREADA_POR,           receta.getCreadaPor())
                .append(ConstantesRecetaRepository.CAMPO_ES_PERSONALIZADA,     receta.isEsPersonalizada())
                .append(ConstantesRecetaRepository.CAMPO_VISIBILIDAD,          receta.getVisibilidad())
                .append(ConstantesRecetaRepository.CAMPO_IMAGEN,               new ArrayList<>());

        getColeccion().insertOne(doc);
        return doc.getObjectId(ConstantesRecetaRepository.CAMPO_ID);
    }

    public boolean actualizar(String id, Document campos) {
        Document filtro = new Document(ConstantesRecetaRepository.CAMPO_ID, new ObjectId(id));
        Document update = new Document("$set", campos);
        return getColeccion().updateOne(filtro, update).getModifiedCount() > 0;
    }

    public boolean agregarImagen(String recetaId, ObjectId imagenId) {
        Document filtro = new Document(ConstantesRecetaRepository.CAMPO_ID, new ObjectId(recetaId));
        Document update = new Document("$push", new Document(ConstantesRecetaRepository.CAMPO_IMAGEN, imagenId));
        return getColeccion().updateOne(filtro, update).getModifiedCount() > 0;
    }

    public boolean eliminarImagen(String recetaId, ObjectId imagenId) {
        Document filtro = new Document(ConstantesRecetaRepository.CAMPO_ID, new ObjectId(recetaId));
        Document update = new Document("$pull", new Document(ConstantesRecetaRepository.CAMPO_IMAGEN, imagenId));
        return getColeccion().updateOne(filtro, update).getModifiedCount() > 0;
    }

    public boolean eliminar(String id) {
        Document filtro = new Document(ConstantesRecetaRepository.CAMPO_ID, new ObjectId(id));
        return getColeccion().deleteOne(filtro).getDeletedCount() > 0;
    }
}
