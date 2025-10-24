//package com.app.taskmanager.configuration;
//
//import com.app.taskmanager.dto.response.ResponseDto;
//import io.swagger.v3.oas.models.media.Content;
//import io.swagger.v3.oas.models.media.Schema;
//import io.swagger.v3.oas.models.responses.ApiResponse;
//import org.springdoc.core.customizers.OperationCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * Configuration class for customizing OpenAPI/Swagger documentation.
// * <p>
// * Wraps successful responses in a standardized {@link ResponseDto} structure and adds
// * common error responses (e.g., 400, 404, 500) with consistent formatting.
// */
//@Configuration
//public class OpenApiConfiguration {
//
//    /**
//     * Provides a customizer for all OpenAPI operations.
//     * <p>
//     * - Wraps 2xx responses in a {@code ResponseDto} structure with fields:
//     *   - data: actual response payload
//     *   - message: informational message
//     *   - timestamp: response timestamp
//     * - Adds standardized error responses for 400, 404, and 500 HTTP status codes.
//     *
//     * @return an {@link OperationCustomizer} bean for customizing OpenAPI operations
//     */
//    @Bean
//    public OperationCustomizer operationCustomizer() {
//        return (operation, handlerMethod) -> {
//
//            Class<?> returnType = handlerMethod.getReturnType().getParameterType();
//
//            // Skip wrapping if the return type is already ResponseDto
//            if (returnType.equals(ResponseDto.class)) {
//                return operation;
//            }
//
//            // Wrap successful responses (2xx) in ResponseDto format
//            operation.getResponses().forEach((statusCode, apiResponse) -> {
//                if (statusCode.startsWith("2")) {
//                    if (apiResponse.getContent() != null) {
//                        apiResponse.getContent().forEach((mediaType, content) -> {
//                            var schema = content.getSchema();
//                            var ref = schema.get$ref();
//                            if (schema != null) {
//
//                                System.out.println("----------------------" + ref + "----------------------");
//
//                                    // wyciągamy nazwę po ostatnim "/"
//                                    var name = ref.substring(ref.lastIndexOf('/') + 1);
//                                System.out.println("----------------------" +name+ "----------------------");
//                                    if (name.equals("ResponseDto")) {
//                                        // tylko dokładnie ResponseDto, np. TaskResponseDto nie pasuje
//                                        return;
//                                    }
//
//                                Schema<?> wrappedSchema = new Schema<>()
//                                        .addProperty("data", content.getSchema())
//                                        .addProperty("message", new Schema<>().type("string"))
//                                        .addProperty("timestamp", new Schema<>().type("string").format("date-time"));
//
//                                content.setSchema(wrappedSchema);
//                            }
//                        });
//                    }
//                }
//            });
//
//            // Add common error responses
//            addErrorResponse(operation, "400", "Bad Request", "Invalid request");
//            addErrorResponse(operation, "500", "Internal Server Error", "Internal server error");
//
//            // Add 404 responses based on handler method naming conventions
//            if (handlerMethod.getMethod().getName().toLowerCase().contains("task")) {
//                addErrorResponse(operation, "404", "Task not found", "Task not found");
//            }
//
//            if (handlerMethod.getMethod().getName().toLowerCase().contains("user")) {
//                addErrorResponse(operation, "404", "User not found", "User not found");
//            }
//
//            return operation;
//        };
//    }
//
//    /**
//     * Helper method to add a standardized error response to an OpenAPI operation.
//     *
//     * @param operation   the OpenAPI operation to which the error response will be added
//     * @param code        the HTTP status code (e.g., "400", "404", "500")
//     * @param description a short description of the error
//     * @param message     the message to include in the ResponseDto's "message" field
//     */
//    private void addErrorResponse(io.swagger.v3.oas.models.Operation operation,
//                                  String code,
//                                  String description,
//                                  String message) {
//        var response = new ApiResponse()
//                .description(description)
//                .content(new Content().addMediaType("application/json",
//                        new io.swagger.v3.oas.models.media.MediaType().schema(
//                                new Schema<ResponseDto>()
//                                        .addProperty("data", new Schema<>().nullable(true))
//                                        .addProperty("message", new Schema<>().example(message))
//                                        .addProperty("timestamp", new Schema<>().type("string").format("date-time"))
//                        )));
//
//        operation.getResponses().put(code, response);
//    }
//
//}
