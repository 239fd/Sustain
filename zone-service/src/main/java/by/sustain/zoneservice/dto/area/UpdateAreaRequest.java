package by.sustain.zoneservice.dto.area;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

@Schema(description = "Request to update an area (role)")
public record UpdateAreaRequest(
        @Schema(description = "User identifier who owns the area", example = "8f2c1b9d-55aa-4d22-9c11-998877665544", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "userId is required")
        UUID userId,

        @Schema(description = "New area name", example = "Strategy")
        String name,

        @Schema(description = "New HEX color for visual display", example = "#00AAFF")
        @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Invalid HEX color format")
        String color,

        @Schema(description = "New energy level", example = "MEDIUM", allowableValues = {"LOW", "MEDIUM", "HIGH"})
        @Pattern(regexp = "^(HIGH|MEDIUM|LOW)$", message = "Energy must be HIGH, MEDIUM or LOW")
        String energy,

        @Schema(description = "Flag indicating if the area is blocked", example = "true")
        Boolean isBlocked
) {
}
