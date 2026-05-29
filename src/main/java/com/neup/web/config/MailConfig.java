package com.neup.web.config;

import com.neup.web.utils.ConfigurationReader;
import com.sendgrid.SendGrid;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.neup.web.utils.ConstantesEntorno.SENDGRID_API_KEY;

@Configuration
public class MailConfig {
    @Bean
    public SendGrid sendGrid() {
        return new SendGrid(ConfigurationReader.getProperty(SENDGRID_API_KEY));
    }
}
