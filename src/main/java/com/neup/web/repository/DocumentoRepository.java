package com.neup.web.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.neup.web.integration.ConnectionFactory;
import com.neup.web.utils.repository.ConstantesDocumentoRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DocumentoRepository {

    private MongoCollection<Document> getColeccion() {
        MongoDatabase db = ConnectionFactory.getDatabase();
        return db.getCollection(ConstantesDocumentoRepository.COLECCION);
    }

    public Optional<Document> findById(String id) {
        Document filtro = new Document(ConstantesDocumentoRepository.CAMPO_ID, new ObjectId(id));
        return Optional.ofNullable(getColeccion().find(filtro).first());
    }

    public ObjectId insertar(String nombre, long tamanno, String ruta, String extension) {
        Document doc = new Document()
                .append(ConstantesDocumentoRepository.CAMPO_NOMBRE,    nombre)
                .append(ConstantesDocumentoRepository.CAMPO_TAMANNO,   tamanno)
                .append(ConstantesDocumentoRepository.CAMPO_RUTA,      ruta)
                .append(ConstantesDocumentoRepository.CAMPO_EXTENSION, extension);

        getColeccion().insertOne(doc);
        return doc.getObjectId(ConstantesDocumentoRepository.CAMPO_ID);
    }

    public boolean eliminar(String id) {
        Document filtro = new Document(ConstantesDocumentoRepository.CAMPO_ID, new ObjectId(id));
        return getColeccion().deleteOne(filtro).getDeletedCount() > 0;
    }
}
