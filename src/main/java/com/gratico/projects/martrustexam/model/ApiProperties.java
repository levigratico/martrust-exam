package com.gratico.projects.martrustexam.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api")
@Data
public class ApiProperties {
    private String baseUrl;
    private String key;
}
