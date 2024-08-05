package odiro.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

//@OpenAPIDefinition(
//        info = @Info(title = "ODIRO Swagger",
//                description = "ODIRO API 명세서",
//                version = "v1.0.0")
//)
//@SecurityScheme(
//        type = SecuritySchemeType.APIKEY,
//        in = SecuritySchemeIn.HEADER,
//        name = "Authorization", description = "Access Token"
//)
//@SecurityScheme(
//        type = SecuritySchemeType.APIKEY,
//        in = SecuritySchemeIn.HEADER,
//        name = "Authorization-refresh", description = "Refresh Token"
//)
//@Configuration
//public class SwaggerConfig {
//    @Bean
//    public GroupedOpenApi openApi() {
//        return GroupedOpenApi.builder()
//                .group("api")
//                .addOpenApiCustomizer(oac -> oac
//                        .security(List.of(new SecurityRequirement().addList("Authorization"),
//                                new SecurityRequirement().addList("Authorization-refresh"))))
//                .pathsToMatch("/**").build();
//    }
//}

@OpenAPIDefinition(
        info = @Info(title = "ODIRO Swagger",
                description = "ODIRO API 명세서",
                version = "v1.0.0")
)

@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi openApi() {
        return GroupedOpenApi.builder()
                .group("api")
                .addOpenApiCustomizer(oac -> oac
                        .security(List.of(new SecurityRequirement().addList("Bearer Authentication"))))
                .pathsToMatch("/**").build();
    }
}
//@Configuration
//public class SwaggerConfig {
//
//    @Bean
//    public OpenAPI openAPI() {
//        String jwt = "JWT";
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
//        SecurityScheme securityScheme = new SecurityScheme()
//                .name(DEFAULT_AUTH)
//                .type(SecurityScheme.Type.HTTP)
//                .in(SecurityScheme.In.HEADER)
//                .scheme("bearer")
//                .bearerFormat("JWT");
//
//        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
//                .name(jwt)
//                .type(SecurityScheme.Type.HTTP)
//                .scheme("bearer")
//                .bearerFormat("JWT")
//        );
//        return new OpenAPI()
//                .components(new Components())
//                .info(apiInfo())
//                .addSecurityItem(securityRequirement)
//                .components(components);
//    }
//    private Info apiInfo() {
//        return new Info()
//                .title("API Test") // API의 제목
//                .description("Let's practice Swagger UI") // API에 대한 설명
//                .version("1.0.0"); // API의 버전
//    }
//}