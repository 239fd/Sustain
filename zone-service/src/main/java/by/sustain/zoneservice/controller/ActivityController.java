package by.sustain.zoneservice.controller;

import by.sustain.zoneservice.dto.activity.ActivityListResponse;
import by.sustain.zoneservice.dto.activity.ActivityResponse;
import by.sustain.zoneservice.dto.activity.CreateActivityRequest;
import by.sustain.zoneservice.dto.activity.UpdateActivityRequest;
import by.sustain.zoneservice.exception.ErrorResponse;
import by.sustain.zoneservice.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/zone/activities")
@RequiredArgsConstructor
@Tag(name = "Activities", description = "API for managing activities within user roles (areas)")
public class ActivityController {

    private final ActivityService activityService;

    @Operation(
            summary = "Create a new activity",
            description = "Creates a new activity linked to a specific area (role)",
            parameters = {
                    @Parameter(name = "X-Request-ID", description = "Unique request identifier for tracing", required = true, in = ParameterIn.HEADER, example = "9988aabb-ccdd-eeff-1122-334455667788")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Activity successfully created",
                    content = @Content(schema = @Schema(implementation = ActivityResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data (empty name, invalid areaId)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Area not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Activity with this name already exists in the area",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<ActivityResponse> create(
            @Valid @RequestBody CreateActivityRequest request) {

        ActivityResponse response = activityService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get all activities by area",
            description = "Returns all activities belonging to the specified area (role)",
            parameters = {
                    @Parameter(name = "X-Request-ID", description = "Unique request identifier for tracing", required = true, in = ParameterIn.HEADER, example = "aabbccdd-1111-2222-3333-444455556666")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of activities successfully retrieved",
                    content = @Content(schema = @Schema(implementation = ActivityListResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid areaId format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Area not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<ActivityListResponse> getAllByArea(
            @Parameter(description = "Area identifier", required = true, example = "3f1c2a9b-55aa-4d22-9c11-998877665544")
            @RequestParam("area_id") UUID areaId) {

        ActivityListResponse response = activityService.getAllByAreaId(areaId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update an activity",
            description = "Updates parameters of an existing activity within an area",
            parameters = {
                    @Parameter(name = "X-Request-ID", description = "Unique request identifier for tracing", required = true, in = ParameterIn.HEADER, example = "11223344-5566-7788-99aa-bbccddeeff00")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Activity successfully updated",
                    content = @Content(schema = @Schema(implementation = ActivityResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data (empty name)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Activity not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Activity with this name already exists in the area",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PutMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> update(
            @Parameter(description = "Activity identifier", required = true, example = "5a4b3c2d-1111-2222-3333-abcdefabcdef")
            @PathVariable UUID activityId,
            @Valid @RequestBody UpdateActivityRequest request) {

        ActivityResponse response = activityService.update(activityId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete an activity",
            description = "Deletes an activity from an area",
            parameters = {
                    @Parameter(name = "X-Request-ID", description = "Unique request identifier for tracing", required = true, in = ParameterIn.HEADER, example = "55667788-99aa-bbcc-ddee-ff0011223344")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Activity successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Activity not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/{activityId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Activity identifier", required = true, example = "5a4b3c2d-1111-2222-3333-abcdefabcdef")
            @PathVariable UUID activityId) {
        activityService.delete(activityId);
        return ResponseEntity.noContent().build();
    }
}
