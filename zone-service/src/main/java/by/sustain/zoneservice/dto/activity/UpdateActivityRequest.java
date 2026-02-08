package by.sustain.zoneservice.dto.activity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to update an activity")
public record UpdateActivityRequest(
        @Schema(description = "New activity name", example = "OKR Planning", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Name is required")
        String name
) {
}
