package com.neup.web.service;

import org.springframework.stereotype.Service;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

@Service
public class EmailVerificacionService {

    private static final String DNS_FACTORY = "com.sun.jndi.dns.DnsContextFactory";

    /**
     * Verifica que el dominio del email tenga registros MX en el DNS.
     * Un dominio sin registros MX no puede recibir correos, por lo que
     * la dirección es inválida aunque su formato sea correcto.
     */
    public boolean dominioAceptaCorreos(String email) {
        if (email == null || !email.contains("@")) return false;

        String dominio = email.substring(email.lastIndexOf('@') + 1).trim();
        if (dominio.isBlank()) return false;

        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", DNS_FACTORY);
            env.put("java.naming.provider.url", "dns:");
            env.put("com.sun.jndi.dns.timeout.initial", "5000");
            env.put("com.sun.jndi.dns.timeout.retries", "1");

            InitialDirContext ctx = new InitialDirContext(env);
            Attributes attrs = ctx.getAttributes(dominio, new String[]{"MX"});
            Attribute mx = attrs.get("MX");
            return mx != null && mx.size() > 0;

        } catch (Exception e) {
            return false;
        }
    }
}
