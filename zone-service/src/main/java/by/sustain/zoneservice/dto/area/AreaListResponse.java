package by.sustain.zoneservice.dto.area;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response containing list of user areas")
public record AreaListResponse(
        @Schema(description = "List of user areas (roles)")
        List<AreaResponse> areas
) {
}
