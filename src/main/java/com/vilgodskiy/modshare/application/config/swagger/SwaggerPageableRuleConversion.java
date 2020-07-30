package com.vilgodskiy.modshare.application.config.swagger;

import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.AlternateTypeBuilder;
import springfox.documentation.builders.AlternateTypePropertyBuilder;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * @author Vilgodskiy Sergey 24.07.2020
 *
 * Configuration for working with pageable in Swagger
 */
@RequiredArgsConstructor
public class SwaggerPageableRuleConversion implements AlternateTypeRuleConvention {

    private final SpringDataWebProperties webProperties;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public List<AlternateTypeRule> rules() {
        return singletonList(
                newRule(Pageable.class, pageableDocumentedType(webProperties.getPageable(), webProperties.getSort()))
        );
    }

    private Type pageableDocumentedType(SpringDataWebProperties.Pageable pageable, SpringDataWebProperties.Sort sort) {
        final String firstPage = pageable.isOneIndexedParameters() ? "1" : "0";
        return new AlternateTypeBuilder()
                .fullyQualifiedClassName(fullyQualifiedName())
                .property(property(pageable.getPageParameter(), Integer.class, new HashMap<>() {{
                    put("value", "Page " + (pageable.isOneIndexedParameters() ? "Number" : "Index"));
                    put("defaultValue", firstPage);
                    put("allowableValues", String.format("range[%s, %s]", firstPage, Integer.MAX_VALUE));
                    put("example", firstPage);
                }}))
                .property(property(pageable.getSizeParameter(), Integer.class, new HashMap<>() {{
                    put("value", "Number of records per page.");
                    put("defaultValue", String.valueOf(pageable.getDefaultPageSize()));
                    put("allowableValues", String.format("range[1, %s]", pageable.getMaxPageSize()));
                    put("example", "10");
                }}))
                .property(property(sort.getSortParameter(), String[].class,
                        Collections.singletonMap("value", "Sorting criteria in the format: property(,asc|desc). "
                                + "Default sort order is ascending. " + "Multiple sort criteria are supported.")

                ))
                .build();
    }

    private String fullyQualifiedName() {
        return String.format("%s.generated.%s", Pageable.class.getPackage().getName(), Pageable.class.getSimpleName());
    }

    private AlternateTypePropertyBuilder property(String name, Class<?> type, Map<String, Object> parameters) {
        return new AlternateTypePropertyBuilder()
                .withName(name)
                .withType(type)
                .withCanRead(true)
                .withCanWrite(true)
                .withAnnotations(Collections.singletonList(AnnotationProxy.of(ApiParam.class, parameters)));
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Accessors(fluent = true)
    public static class AnnotationProxy implements Annotation, InvocationHandler {
        @Getter
        private final Class<? extends Annotation> annotationType;
        private final Map<String, Object> values;

        @SuppressWarnings("unchecked")
        public static <A extends Annotation> A of(Class<A> annotation, Map<String, Object> values) {
            return (A) Proxy.newProxyInstance(annotation.getClassLoader(),
                    new Class[]{annotation},
                    new AnnotationProxy(annotation, new HashMap<>(values) {{
                        put("annotationType", annotation); // Required because getDefaultValue() returns null for this call
                    }}));
        }

        public Object invoke(Object proxy, Method method, Object[] args) {
            return values.getOrDefault(method.getName(), method.getDefaultValue());
        }
    }
}
