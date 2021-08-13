package com.cerqlar.intmatch.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

/**
 * Created by chinnku on Aug, 2021
 */
@Configuration
class Swagger {
    @Bean
    fun swaggerConfiguration(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.cerqlar.intmatch.controller"))
            .build()
            .apiInfo(apiDetails())
    }

    private fun apiDetails(): ApiInfo {
        return ApiInfo(
            "CerQlar - Interest Matching API",
            "This API provides the resource and implementation for matching Buyer’s interests in buying certain quantity of Certificates. The key options are," +
                    "\n1. Create Buyer, Seller and Issuer as Trader resource" +
                    "\n2. Create Interests for Buyer" +
                    "\n3. Create Certificate Bundles for for Sellers" +
                    "\n4. Function to find a match for a given interest" +
                    "\n5. Function to assign a match and close the interest" +
                    "\n\nExplore the API documentation for more details..",
            "1.0",
            "https://cerqlar.com",
            springfox.documentation.service.Contact("Kumar Chinnathambi", "https://cerqlar.com", "kumarc@cerqlar.com"),
            "API License",
            "https://cerqlar.com",
            emptyList()
        )
    }
}