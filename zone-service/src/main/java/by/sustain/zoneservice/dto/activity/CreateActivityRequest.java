package by.sustain.zoneservice.dto.activity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Request to create a new activity within an area")
public record CreateActivityRequest(
        @Schema(description = "Area identifier the activity belongs to", example = "3f1c2a9b-55aa-4d22-9c11-998877665544", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "areaId is required")
        UUID areaId,

        @Schema(description = "Activity name", example = "Quarter Planning", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Name is required")
        String name
) {
}
