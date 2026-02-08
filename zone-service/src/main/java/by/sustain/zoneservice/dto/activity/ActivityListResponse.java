package by.sustain.zoneservice.dto.activity;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response containing list of activities")
public record ActivityListResponse(
        @Schema(description = "List of activities within an area")
        List<ActivityResponse> activities
) {
}
