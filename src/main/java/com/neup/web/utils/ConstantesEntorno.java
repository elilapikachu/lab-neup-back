package com.neup.web.utils;

public class ConstantesEntorno {
    public final static String URL_DB = "URL_DB";
    public final static  String NAME_DB = "NAME_DB";
    public final static  String SPRING_MAIL_USERNAME = "spring.mail.username";
    public final static  String SPRING_MAIL_PASSWORD = "spring.mail.password";
    public static final boolean IS_RENDER = System.getenv("RENDER") != null;
}
