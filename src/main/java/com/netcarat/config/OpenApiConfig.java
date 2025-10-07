package com.netcarat.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${openapi.server.url}")
    private String serverUrl;

    @Value("${openapi.server.description}")
    private String serverDescription;

    @Bean
    public OpenAPI myOpenAPI() {
        Server server = new Server();
        server.setUrl(serverUrl);
        server.setDescription(serverDescription);

        Contact contact = new Contact();
        contact.setName("KPI Backend Team");
        contact.setEmail("info@example.com");
        contact.setUrl("https://www.example.com");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("KPI Management API")
                .version("1.0")
                .description("This API exposes endpoints to manage KPIs.")
                .contact(contact)
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}
