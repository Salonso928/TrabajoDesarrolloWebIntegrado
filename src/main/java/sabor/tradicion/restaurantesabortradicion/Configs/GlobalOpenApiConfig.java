package sabor.tradicion.restaurantesabortradicion.Configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalOpenApiConfig {

    @Bean
    public OpenApiCustomizer globalResponses() {

        return openApi -> {

            // Registrar el esquema global de errores
            if (openApi.getComponents() == null) {
                openApi.setComponents(new Components());
            }

            openApi.getComponents().addSchemas(
                    "ErrorResponse",
                    new Schema<>()
                            .type("object")
                            .description("Estructura estándar de respuesta de error")

                            .addProperty("status",
                                    new Schema<Integer>()
                                            .type("integer")
                                            .description("Código HTTP del error")
                                            .example(400))

                            .addProperty("error",
                                    new Schema<String>()
                                            .type("string")
                                            .description("Tipo de error")
                                            .example("Bad Request"))

                            .addProperty("message",
                                    new Schema<String>()
                                            .type("string")
                                            .description("Mensaje descriptivo del error"))

                            .addProperty("path",
                                    new Schema<String>()
                                            .type("string")
                                            .description("Ruta donde ocurrió el error")
                                            .example("/api/users"))

                            .addProperty("timestamp",
                                    new Schema<String>()
                                            .type("string")
                                            .format("date-time")
                                            .description("Fecha y hora del error"))
            );

            // Esquema común para las respuestas de error
            Content errorContent = new Content()
                    .addMediaType(
                            "application/json",
                            new MediaType().schema(
                                    new Schema<>()
                                            .$ref("#/components/schemas/ErrorResponse")
                            )
                    );

            // Agregar respuestas globales a todos los endpoints
            openApi.getPaths().values().forEach(path ->
                    path.readOperations().forEach(operation -> {

                        operation.getResponses().addApiResponse(
                                "400",
                                new ApiResponse()
                                        .description("Error en los datos enviados o validaciones")
                                        .content(errorContent)
                        );

                        operation.getResponses().addApiResponse(
                                "401",
                                new ApiResponse()
                                        .description("Credenciales incorrectas o usuario no autenticado")
                                        .content(errorContent)
                        );

                        operation.getResponses().addApiResponse(
                                "403",
                                new ApiResponse()
                                        .description("Token inválido, expirado o permisos insuficientes")
                                        .content(errorContent)
                        );

                    })
            );
        };
    }
}
