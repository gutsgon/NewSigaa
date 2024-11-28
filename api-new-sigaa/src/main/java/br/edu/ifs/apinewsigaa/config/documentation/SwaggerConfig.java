package br.edu.ifs.apinewsigaa.config.documentation;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuração para a documentação da API usando Swagger.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configuração do OpenAPI para a API NewSigaa.
     *
     * @return Uma instância de OpenAPI configurada para a API NewSigaa.
     */
    @Bean
    public OpenAPI sysNewSigaaOpenAPI(){
        return new OpenAPI()
                .info(new Info().title("NewSigaa API")
                        .description("Api do Projeto Corporativo do NewSigaa")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("NewSigaa Wiki Documentation")
                        .url("https://newsigaa.github.org/docs"));
    }
}
