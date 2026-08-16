package me.mallahajay43.campaignflow.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI campaignFlowOpenAPI() {

        SecurityScheme bearerScheme = new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        Info info = new Info()
                .title("CampaignFlow API")
                .version("v1")
                .description(
                        "Multi-tenant SaaS email campaign platform. " +
                                "Includes audience management, CSV imports, templates, " +
                                "campaign execution, transactional outbox, Kafka retry/DLT, " +
                                "Redis idempotency and MinIO object storage."
                );

        return new OpenAPI()
                .info(info)
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(SECURITY_SCHEME_NAME)
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        SECURITY_SCHEME_NAME,
                                        bearerScheme
                                )
                );
    }
}
