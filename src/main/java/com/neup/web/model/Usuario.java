package com.neup.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private ObjectId id;
    private String usuario;
    private String email;
    private String password;

    // Constructor sin id (para inserciones)
    public Usuario(String usuario, String email, String password) {
        this.usuario = usuario;
        this.email = email;
        this.password = password;
    }

    // Método auxiliar para obtener id como String
    public String getIdAsString() {
        return id != null ? id.toHexString() : null;
    }
}