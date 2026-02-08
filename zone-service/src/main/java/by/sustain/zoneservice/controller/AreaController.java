package by.sustain.zoneservice.controller;

import by.sustain.zoneservice.dto.area.AreaListResponse;
import by.sustain.zoneservice.dto.area.AreaResponse;
import by.sustain.zoneservice.dto.area.CreateAreaRequest;
import by.sustain.zoneservice.dto.area.UpdateAreaRequest;
import by.sustain.zoneservice.exception.ErrorResponse;
import by.sustain.zoneservice.service.AreaService;
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
@RequestMapping("/api/v1/zone/areas")
@RequiredArgsConstructor
@Tag(name = "Areas", description = "API for managing user roles (areas) with visual parameters and energy levels")
public class AreaController {

    private final AreaService areaService;

    @Operation(
            summary = "Create a new area",
            description = "Creates a new user role (area) with visual parameters and energy level",
            parameters = {
                    @Parameter(name = "X-Request-ID", description = "Unique request identifier for tracing", required = true, in = ParameterIn.HEADER, example = "1b2c3d4e-1111-2222-3333-abcdef123456")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Area successfully created",
                    content = @Content(schema = @Schema(implementation = AreaResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data (empty name, invalid HEX color, invalid energy level)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Area with this name already exists for the user",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<AreaResponse> create(
            @Valid @RequestBody CreateAreaRequest request) {

        AreaResponse response = areaService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get all areas for a user",
            description = "Returns all roles (areas) belonging to the specified user for UI display",
            parameters = {
                    @Parameter(name = "X-Request-ID", description = "Unique request identifier for tracing", required = true, in = ParameterIn.HEADER, example = "7c8d9e00-aaaa-bbbb-cccc-123456789abc")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of areas successfully retrieved",
                    content = @Content(schema = @Schema(implementation = AreaListResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid userId format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<AreaListResponse> getAll(
            @Parameter(description = "User identifier", required = true, example = "8f2c1b9d-55aa-4d22-9c11-998877665544")
            @RequestParam UUID userId) {
        AreaListResponse response = areaService.getAllByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update an area",
            description = "Updates parameters of an existing area (role)",
            parameters = {
                    @Parameter(name = "X-Request-ID", description = "Unique request identifier for tracing", required = true, in = ParameterIn.HEADER, example = "0f1e2d3c-4444-5555-6666-abcdefabcdef")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Area successfully updated",
                    content = @Content(schema = @Schema(implementation = AreaResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data (invalid HEX color, invalid energy level)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied - user is not the owner of the area",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Area not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Area with this name already exists for the user",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PutMapping("/{areaId}")
    public ResponseEntity<AreaResponse> update(
            @Parameter(description = "Area identifier", required = true, example = "3f1c2a9b-55aa-4d22-9c11-998877665544")
            @PathVariable UUID areaId,
            @Valid @RequestBody UpdateAreaRequest request) {

        AreaResponse response = areaService.update(areaId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete an area",
            description = "Deletes an area and all associated activities",
            parameters = {
                    @Parameter(name = "X-Request-ID", description = "Unique request identifier for tracing", required = true, in = ParameterIn.HEADER, example = "123e4567-e89b-12d3-a456-426614174000")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Area successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid authorization token",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied - user is not the owner of the area",
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
    @DeleteMapping("/{areaId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Area identifier", required = true, example = "3f1c2a9b-55aa-4d22-9c11-998877665544")
            @PathVariable UUID areaId,
            @Parameter(description = "User identifier for access verification", required = true, example = "8f2c1b9d-55aa-4d22-9c11-998877665544")
            @RequestParam UUID userId) {

        areaService.delete(areaId, userId);
        return ResponseEntity.noContent().build();
    }
}
