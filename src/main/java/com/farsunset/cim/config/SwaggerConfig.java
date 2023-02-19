package com.farsunset.cim.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@EnableOpenApi
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket userApiDocket(ApiInfo apiInfo) {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo)
                .groupName("客户端接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.farsunset.cim.mvc.controller.api"))
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("CIM Push Service APIs.")
                .description("CIM客户端接口文档")
                .version("3.0")
                .build();
    }

    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> schemeList = new ArrayList<>();
        schemeList.add(new ApiKey("access-token", "access-token", "header"));
        return schemeList;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContextList = new ArrayList<>();
        List<SecurityReference> securityReferenceList = new ArrayList<>();
        securityReferenceList.add(new SecurityReference("access-token", new AuthorizationScope[]{new AuthorizationScope("global", "accessAnything")}));
        securityContextList.add(SecurityContext
                .builder()
                .securityReferences(securityReferenceList)
                .operationSelector(operationContext -> true)
                .build()
        );
        return securityContextList;
    }

}
