package com.neup.web.utils.repository;

public class ConstantesRecetaRepository {

    private ConstantesRecetaRepository() {}

    public static final String COLECCION                    = "receta";

    // Campos raíz
    public static final String CAMPO_ID                     = "_id";
    public static final String CAMPO_NOMBRE_RECETA          = "nombre_receta";
    public static final String CAMPO_INGREDIENTES           = "ingredientes";
    public static final String CAMPO_NUTRICION              = "nutricion";
    public static final String CAMPO_TAGS                   = "tags";
    public static final String CAMPO_TIEMPO_PREPARACION     = "tiempo_preparacion";
    public static final String CAMPO_CREADA_POR             = "creada_por";
    public static final String CAMPO_ES_PERSONALIZADA       = "es_personalizada";
    public static final String CAMPO_VISIBILIDAD            = "visibilidad";
    public static final String CAMPO_IMAGEN                 = "imagen";

    // Ingrediente
    public static final String CAMPO_NOMBRE_INGREDIENTE     = "nombre_ingrediente";
    public static final String CAMPO_CANTIDAD               = "cantidad";
    public static final String CAMPO_TIPO_INGREDIENTE       = "tipo_ingrediente";

    // Nutricion
    public static final String CAMPO_KCAL                   = "kcal";
    public static final String CAMPO_PROTEINAS              = "proteinas";
    public static final String CAMPO_CARBOHIDRATOS          = "carbohidratos";
    public static final String CAMPO_FIBRA                  = "fibra";
    public static final String CAMPO_VITAMINAS              = "vitaminas";
    public static final String CAMPO_MINERALES              = "minerales";

    // Visibilidad
    public static final String VISIBILIDAD_PUBLICA          = "publica";
    public static final String VISIBILIDAD_PRIVADA          = "privada";
}
