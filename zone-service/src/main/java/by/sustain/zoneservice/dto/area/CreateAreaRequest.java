package by.sustain.zoneservice.dto.area;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

@Schema(description = "Request to create a new area (role)")
public record CreateAreaRequest(
        @Schema(description = "User identifier who owns the area", example = "8f2c1b9d-55aa-4d22-9c11-998877665544", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "userId is required")
        UUID userId,

        @Schema(description = "Area name", example = "Strategist", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Name is required")
        String name,

        @Schema(description = "HEX color for visual display", example = "#FFAA00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Color is required")
        @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Invalid HEX color format")
        String color,

        @Schema(description = "Energy level of the area", example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH"}, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Energy is required")
        @Pattern(regexp = "^(HIGH|MEDIUM|LOW)$", message = "Energy must be HIGH, MEDIUM or LOW")
        String energy,

        @Schema(description = "Flag indicating if the area is blocked", example = "false", defaultValue = "false")
        Boolean isBlocked
) {
    public CreateAreaRequest {
        if (isBlocked == null) {
            isBlocked = false;
        }
    }
}
