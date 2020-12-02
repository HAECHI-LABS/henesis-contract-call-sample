package io.haechi.henesis.example.config;

import com.google.common.collect.Ordering;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Operation;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(swaggerInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("io.haechi.henesis.example"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .operationOrdering(new Ordering<Operation>() {
                    @Override
                    public int compare(Operation left, Operation right) {
                        return left.getMethod().name().compareTo(right.getMethod().name());
                    }
                });
    }

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder().title("Henesis Contract Call Example RestAPI Documentation")
                .description("This is a sample project that shows example about how to call contract with Henesis API.")
                .license("HAECHI LABS").version("1").build();
    }
}
