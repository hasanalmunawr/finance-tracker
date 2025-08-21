package hasanalmunawr.dev.backend_spring.web.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sales Management API")
                        .version("1.0.0")
                        .description("""
                                This API provides a full suite of features for managing sales processes, including:
                                
                                - Customer and Product Management
                                - Service Order Handling
                                - Branch Operations
                                - Reporting & Analytics
                                
                                Use the endpoints responsibly and refer to the documentation for required request formats.
                                """)
                        .contact(new Contact()
                                .name("IT Support")
                                .url("https://your-company.com/support")
                                .email("support@your-company.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                )
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development Server"),
                        new Server().url("https://api.your-company.com").description("Production Server")
                ))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Wiki")
                        .url("https://github.com/your-org/sales-management-api/wiki")
                );
    }

}
