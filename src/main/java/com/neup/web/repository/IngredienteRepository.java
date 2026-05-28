package com.neup.web.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.neup.web.integration.ConnectionFactory;
import com.neup.web.utils.repository.ConstantesIngredienteRepository;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class IngredienteRepository {

    private MongoCollection<Document> getColeccion() {
        MongoDatabase db = ConnectionFactory.getDatabase();
        return db.getCollection(ConstantesIngredienteRepository.COLECCION);
    }

    public List<Document> findAll() {
        return getColeccion().find().into(new ArrayList<>());
    }
}
