package com.tms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.tms.util.UtilProperties.*;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    Contact contact = new Contact(
            APPLICATION_CONTACT_NAME,
            APPLICATION_API_DOC,
            APPLICATION_CONTACT_EMAIL);

    List<VendorExtension> vendorExtensions = new ArrayList<>();

    ApiInfo apiInfo = new ApiInfo(
            APPLICATION_TITLE,
            APPLICATION_DESCRIPTION,
            APPLICATION_VERSION,
            APPLICATION_TERM_OF_USE_URL,
            contact,
            APPLICATION_LICENSE_TITLE,
            APPLICATION_LICENSE_URL,
            vendorExtensions);

    @Bean
    public Docket api()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .protocols(new HashSet<>(Arrays.asList("http", "https"))).apiInfo(apiInfo).select()
                .apis(RequestHandlerSelectors.basePackage("com.training.erp.controller"))
                .paths(PathSelectors.any()).build();
    }
}
