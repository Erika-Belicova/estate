package com.openclassrooms.estate_back_end.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@SecurityScheme(name = "Authorization", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:3001");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Erika Belicova");

        Info information = new Info().title("Estate Application API")
            .version("1.0")
            .description("This API exposes endpoints to manage rentals.")
            .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }

}