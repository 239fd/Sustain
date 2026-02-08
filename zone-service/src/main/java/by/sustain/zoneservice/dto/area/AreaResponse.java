package by.sustain.zoneservice.dto.area;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Area (role) response")
public record AreaResponse(
        @Schema(description = "Area identifier", example = "3f1c2a9b-55aa-4d22-9c11-998877665544")
        UUID id,

        @Schema(description = "Owner user identifier", example = "8f2c1b9d-55aa-4d22-9c11-998877665544")
        UUID userId,

        @Schema(description = "Area name", example = "Strategist")
        String name,

        @Schema(description = "HEX color for visual display", example = "#FFAA00")
        String color,

        @Schema(description = "Energy level", example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH"})
        String energy,

        @Schema(description = "Flag indicating if the area is blocked", example = "false")
        boolean isBlocked
) {
}
