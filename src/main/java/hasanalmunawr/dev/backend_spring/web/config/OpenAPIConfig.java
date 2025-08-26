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

        Server currentServer = new io.swagger.v3.oas.models.servers.Server();
        currentServer.setDescription("current");
        currentServer.setUrl("/");

        return new OpenAPI()
                .info(new Info()
                        .title("Finance Tracker API")
                        .version("1.1.2")
                        .description("""
                                The Finance Tracker API provides a complete set of features 
                                to help you manage and monitor your financial activities.

                                **Features:**
                                - Record Transactions
                                - Bank Account Records
                                - Transaction Categories
                                - Budgeting
                                - Debt Records

                                Use the endpoints responsibly and refer to this documentation 
                                for details on request formats and usage guidelines.
                                """)
                        .contact(new Contact()
                                .name("Developer")
                                .url("https://hasanalmunawar.my.id")
                                .email("hasanalmunawar.it@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                )
                .servers(
                        List.of(
                               /* new Server()
                                        .url("http://localhost:8080")
                                        .description("Local Development Server"),
                                new Server()
                                        .url("https://fintrack-api.hasanalmunawar.my.id")
                                        .description("Production Server")*/
                                currentServer
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Project Wiki")
                        .url("https://github.com/your-org/sales-management-api/wiki"));
    }

}
