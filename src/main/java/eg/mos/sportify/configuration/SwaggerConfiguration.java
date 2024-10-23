package eg.mos.sportify.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


/**
 * Configuration class for setting up Swagger documentation.

 * Swagger allows for the generation of API documentation
 * for the controllers in the application.
 */
@Configuration
public class SwaggerConfiguration {


    /**
     * Bean definition for creating a Docket object for Swagger 2 documentation.

     * The Docket object is configured to scan the controller package
     * {@code eg.mos.sportify.controller} and document all paths.
     *
     * @return a Docket configured for Swagger 2 documentation.
     */
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("eg.mos.sportify.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
