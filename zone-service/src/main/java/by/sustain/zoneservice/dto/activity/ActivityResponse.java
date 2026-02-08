package by.sustain.zoneservice.dto.activity;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Activity response")
public record ActivityResponse(
        @Schema(description = "Activity identifier", example = "5a4b3c2d-1111-2222-3333-abcdefabcdef")
        UUID id,

        @Schema(description = "Area identifier the activity belongs to", example = "3f1c2a9b-55aa-4d22-9c11-998877665544")
        UUID areaId,

        @Schema(description = "Activity name", example = "Quarter Planning")
        String name
) {
}
