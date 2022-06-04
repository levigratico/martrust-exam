package com.gratico.projects.martrustexam;

import com.gratico.projects.martrustexam.model.ApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
@RequiredArgsConstructor
public class MartrustExamApplication {


    private final ApiProperties apiProperties;


    public static void main(String[] args) {
        SpringApplication.run(MartrustExamApplication.class, args);
    }

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(apiProperties.getBaseUrl()).defaultHeader("apikey", apiProperties.getKey()).build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

}
