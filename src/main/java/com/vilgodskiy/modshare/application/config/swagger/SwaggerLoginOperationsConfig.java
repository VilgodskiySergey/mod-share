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
                        .tags(Collections.singleton("Авторизация"))
                        .consumes(Collections.singleton(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
                                                .modelRef(new ModelRef("LoginForm"))
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
        properties.put("login", new ModelProperty(
                "login", typeResolver.resolve(String.class), "java.lang.String",
                0, true, false, true, false,
                "login", null, "", null, null, null,
                Collections.emptyList()).updateModelRef(input -> new ModelRef("string")));
        properties.put("password", new ModelProperty(
                "password", typeResolver.resolve(String.class), "java.lang.String",
                1, true, false, true, false,
                "password", null, "", null, null, null,
                Collections.emptyList()).updateModelRef(input -> new ModelRef("string")));

        Map<String, Model> loginFormModel = new HashMap<>();
        loginFormModel.put("LoginForm",
                new Model("LoginForm",
                        "LoginForm",
                        typeResolver.resolve(LoginForm.class),
                        "com.vilgodskiy.modshare.application.config.security.domain.LoginForm",
                        properties,
                        "login form dto",
                        "",
                        "",
                        Collections.emptyList(),
                        new LoginForm("admin", "123456"),
                        null)
        );

        def.put("authentication", new ApiListingBuilder(context.getDocumentationContext().getApiDescriptionOrdering())
                .apis(apis)
                .description("Authentication")
                .tags(Collections.singleton(new Tag("Авторизация", "Api для авторизации")))
                .models(loginFormModel)
                .build());
        return def;
    }
}
