package com.neup.web.model;

import org.bson.types.ObjectId;
import java.util.List;

public class Persona {

    private ObjectId id;
    private List<String> nombres;
    private List<String> apellidos;
    private Contactos contactos;
    private CaracteristicasFisicas caracteristicasFisicas;
    private Preferencias preferencias;
    private ObjectId usuarioId;
    private Dietas dietas;
    private Recetas recetas;

    // ── Clases internas ──────────────────────────────────────

    public static class Contactos {
        private Long telefono;
        private String otroEmail;

        public Long getTelefono() { return telefono; }
        public void setTelefono(Long telefono) { this.telefono = telefono; }
        public String getOtroEmail() { return otroEmail; }
        public void setOtroEmail(String otroEmail) { this.otroEmail = otroEmail; }
    }

    public static class CaracteristicasFisicas {
        private Double peso;
        private Double altura;

        public Double getPeso() { return peso; }
        public void setPeso(Double peso) { this.peso = peso; }
        public Double getAltura() { return altura; }
        public void setAltura(Double altura) { this.altura = altura; }
    }

    public static class Preferencias {
        private List<String> gustos;
        private List<String> alergias;
        private List<String> tipoDieta;
        private List<String> objetivos;

        public List<String> getGustos() { return gustos; }
        public void setGustos(List<String> gustos) { this.gustos = gustos; }
        public List<String> getAlergias() { return alergias; }
        public void setAlergias(List<String> alergias) { this.alergias = alergias; }
        public List<String> getTipoDieta() { return tipoDieta; }
        public void setTipoDieta(List<String> tipoDieta) { this.tipoDieta = tipoDieta; }
        public List<String> getObjetivos() { return objetivos; }
        public void setObjetivos(List<String> objetivos) { this.objetivos = objetivos; }
    }

    public static class Dietas {
        private List<ObjectId> guardadas;
        private List<ObjectId> personalizadas;

        public List<ObjectId> getGuardadas() { return guardadas; }
        public void setGuardadas(List<ObjectId> guardadas) { this.guardadas = guardadas; }
        public List<ObjectId> getPersonalizadas() { return personalizadas; }
        public void setPersonalizadas(List<ObjectId> personalizadas) { this.personalizadas = personalizadas; }
    }

    public static class Recetas {
        private List<ObjectId> guardadas;
        private List<ObjectId> personalizadas;

        public List<ObjectId> getGuardadas() { return guardadas; }
        public void setGuardadas(List<ObjectId> guardadas) { this.guardadas = guardadas; }
        public List<ObjectId> getPersonalizadas() { return personalizadas; }
        public void setPersonalizadas(List<ObjectId> personalizadas) { this.personalizadas = personalizadas; }
    }

    // ── Getters y Setters principales ────────────────────────

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public List<String> getNombres() { return nombres; }
    public void setNombres(List<String> nombres) { this.nombres = nombres; }

    public List<String> getApellidos() { return apellidos; }
    public void setApellidos(List<String> apellidos) { this.apellidos = apellidos; }

    public Contactos getContactos() { return contactos; }
    public void setContactos(Contactos contactos) { this.contactos = contactos; }

    public CaracteristicasFisicas getCaracteristicasFisicas() { return caracteristicasFisicas; }
    public void setCaracteristicasFisicas(CaracteristicasFisicas c) { this.caracteristicasFisicas = c; }

    public Preferencias getPreferencias() { return preferencias; }
    public void setPreferencias(Preferencias preferencias) { this.preferencias = preferencias; }

    public ObjectId getUsuarioId() { return usuarioId; }
    public void setUsuarioId(ObjectId usuarioId) { this.usuarioId = usuarioId; }

    public Dietas getDietas() { return dietas; }
    public void setDietas(Dietas dietas) { this.dietas = dietas; }

    public Recetas getRecetas() { return recetas; }
    public void setRecetas(Recetas recetas) { this.recetas = recetas; }
}
