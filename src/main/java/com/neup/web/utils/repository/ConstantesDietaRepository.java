package com.neup.web.utils.repository;

public class ConstantesDietaRepository {

    private ConstantesDietaRepository() {}

    public static final String COLECCION                = "dieta";

    // Campos raíz
    public static final String CAMPO_ID                 = "_id";
    public static final String CAMPO_NOMBRE_DIETA       = "nombre_dieta";
    public static final String CAMPO_DESCRIPCION        = "descripcion";
    public static final String CAMPO_METAS              = "metas";
    public static final String CAMPO_PLAN_SEMANAL       = "plan_semanal";
    public static final String CAMPO_ES_PERSONALIZADA   = "es_personalizada";
    public static final String CAMPO_VISIBILIDAD        = "visibilidad";
    public static final String CAMPO_PORTADA            = "portada";

    // Plan semanal
    public static final String CAMPO_RECETA_ID          = "receta_id";
    public static final String CAMPO_TIPO_COMIDA        = "tipo_comida";

    // Visibilidad
    public static final String VISIBILIDAD_PUBLICA      = "publica";
    public static final String VISIBILIDAD_PRIVADA      = "privada";
}
