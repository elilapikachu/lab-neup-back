package com.neup.web.service;

import com.neup.web.dto.IngredienteDTO;
import com.neup.web.repository.IngredienteRepository;
import com.neup.web.utils.repository.ConstantesIngredienteRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;

    public IngredienteService(IngredienteRepository ingredienteRepository) {
        this.ingredienteRepository = ingredienteRepository;
    }

    public List<IngredienteDTO.IngredienteResponse> obtenerTodos() {
        return ingredienteRepository.findAll().stream()
                .map(this::mapearResponse)
                .toList();
    }

    private IngredienteDTO.IngredienteResponse mapearResponse(Document doc) {
        ObjectId oid = doc.getObjectId(ConstantesIngredienteRepository.CAMPO_ID);
        return IngredienteDTO.IngredienteResponse.builder()
                .id(oid != null ? oid.toHexString() : null)
                .nombreIngrediente(doc.getString(ConstantesIngredienteRepository.CAMPO_NOMBRE))
                .tipoCantidad(doc.getString(ConstantesIngredienteRepository.CAMPO_TIPO_CANTIDAD))
                .tipoIngrediente(doc.getString(ConstantesIngredienteRepository.CAMPO_TIPO_INGREDIENTE))
                .build();
    }
}
