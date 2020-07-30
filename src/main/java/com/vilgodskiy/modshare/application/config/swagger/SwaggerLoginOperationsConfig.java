package com.vilgodskiy.modshare.application.config.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Multimap;
import com.vilgodskiy.modshare.application.config.security.domain.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiListingBuilder;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.Model;
import springfox.documentation.schema.ModelProperty;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Operation;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.spring.web.scanners.ApiDescriptionReader;
import springfox.documentation.spring.web.scanners.ApiListingScanner;
import springfox.documentation.spring.web.scanners.ApiListingScanningContext;
import springfox.documentation.spring.web.scanners.ApiModelReader;

import java.util.*;

import static com.google.common.collect.Sets.newHashSet;

/**
 * @author Vilgodskiy Sergey 24.07.2020
 */
public class SwaggerLoginOperationsConfig extends ApiListingScanner {

    @Autowired
    private TypeResolver typeResolver;

    @Autowired
    SwaggerLoginOperationsConfig(ApiDescriptionReader apiDescriptionReader, ApiModelReader apiModelReader,
                                 DocumentationPluginsManager pluginsManager) {
        super(apiDescriptionReader, apiModelReader, pluginsManager);
    }

    @Override
    public Multimap<String, ApiListing> scan(ApiListingScanningContext context) {
        final Multimap<String, ApiListing> def = super.scan(context);
        final List<ApiDescription> apis = new LinkedList<>();
        final List<Operation> loginOperation = new ArrayList<>();

        loginOperation.add(
                new OperationBuilder(new CachingOperationNameGenerator())
                        .tags(Collections.singleton("auth-api"))
                        .consumes(Collections.singleton(MediaType.APPLICATION_JSON_VALUE))
                        .method(HttpMethod.POST)
                        .uniqueId("login")
                        .parameters(
                                Collections.singletonList(
                                        new ParameterBuilder()
                                                .name("credentionals")
                                                .parameterType("body")
                                                .description("credentionals for login")
                                                .type(typeResolver.resolve(String.class))
                                                .required(true)
                                                .modelRef(new ModelRef("LoginFormDto"))
                                                .build()))
                        .responseMessages(newHashSet(new ResponseMessageBuilder()
                                .code(200)
                                .message("OK")
                                .responseModel(null)
                                .build()))
                        .build()
        );

        apis.add(new ApiDescription(null, "/login", "Authentication documentation", loginOperation, false));

        Map<String, ModelProperty> properties = new HashMap<>();
        properties.put("username", new ModelProperty(
                "username", typeResolver.resolve(String.class), "java.lang.String",
                0, true, false, true, false,
                "username", null, "", null, null, null,
                Collections.emptyList()).updateModelRef(input -> new ModelRef("string")));
        properties.put("password", new ModelProperty(
                "password", typeResolver.resolve(String.class), "java.lang.String",
                1, true, false, true, false,
                "password", null, "", null, null, null,
                Collections.emptyList()).updateModelRef(input -> new ModelRef("string")));

        Map<String, Model> loginFormModel = new HashMap<>();
        loginFormModel.put("LoginFormDto",
                new Model("LoginFormDto",
                        "LoginFormDto",
                        typeResolver.resolve(LoginForm.class),
                        "ru.rtlabs.r2.webapp._config.security.LoginFormDto",
                        properties,
                        "login form dto",
                        "",
                        "",
                        Collections.emptyList(),
                        new LoginForm("username", "password"),
                        null)
        );

        def.put("authentication", new ApiListingBuilder(context.getDocumentationContext().getApiDescriptionOrdering())
                .apis(apis)
                .description("Authentication")
                .tags(Collections.singleton(new Tag("auth-api", "Api for login")))
                .models(loginFormModel)
                .build());
        return def;
    }
}
