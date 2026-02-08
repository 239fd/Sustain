package by.sustain.zoneservice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Error response object")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        @Schema(description = "Timestamp of the error", example = "2026-01-29T12:30:00Z")
        Instant timestamp,

        @Schema(description = "HTTP status code", example = "400")
        int status,

        @Schema(description = "HTTP status reason phrase", example = "Bad Request")
        String error,

        @Schema(description = "Error code", example = "INVALID_INPUT_DATA")
        String code,

        @Schema(description = "Error message", example = "Invalid input data")
        String message,

        @Schema(description = "Detailed error description", example = "name: Name is required")
        String details,

        @Schema(description = "Request path", example = "/api/v1/zone/areas")
        String path
) {
    public static ErrorResponse of(int status, String error, ErrorCode errorCode, String details, String path) {
        return new ErrorResponse(
                Instant.now(),
                status,
                error,
                errorCode.getCode(),
                errorCode.getDefaultMessage(),
                details,
                path
        );
    }
}
