package com.vilgodskiy.modshare.application.config.swagger;

import com.google.common.base.Predicates;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.scanners.ApiDescriptionReader;
import springfox.documentation.spring.web.scanners.ApiListingScanner;
import springfox.documentation.spring.web.scanners.ApiModelReader;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.List;

/**
 * @author Vilgodskiy Sergey 24.07.2020
 */
@Profile({"local", "dev", "test", "demo"})
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${spring.profiles:\"\"}")
    private String profile;

    @Bean
    public Docket api(ServletContext servletContext) {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathProvider(new RelativePathProvider(servletContext) {
                    @Override
                    public String getApplicationBasePath() {
                        return profile.equals("dev") | profile.equals("test") ? "/webapp" : "";
                    }
                })
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(Predicates.not(PathSelectors.regex("/error")))
                .build()
                .useDefaultResponseMessages(false)
                .securitySchemes(
                        Collections.singletonList(
                                new ApiKey("Bearer %token", "Authorization", "Header")))
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(new ApiKey("JWT", HttpHeaders.AUTHORIZATION, "header")));
    }

    @Primary
    @Bean
    public ApiListingScanner addExtraOperations(ApiDescriptionReader apiDescriptionReader, ApiModelReader apiModelReader,
                                                DocumentationPluginsManager pluginsManager) {
        return new SwaggerLoginOperationsConfig(apiDescriptionReader, apiModelReader, pluginsManager);
    }

    @Bean
    public AlternateTypeRuleConvention pageableConversion(SpringDataWebProperties webProperties) {
        return new SwaggerPageableRuleConversion(webProperties);
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{
                new AuthorizationScope("global", "accessEverything")
        };
        return List.of(new SecurityReference("JWT", authorizationScopes));
    }
}
