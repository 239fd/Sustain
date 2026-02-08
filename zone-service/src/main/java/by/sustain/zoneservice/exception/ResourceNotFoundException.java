package by.sustain.zoneservice.exception;

import java.util.UUID;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(ErrorCode errorCode, String details) {
        super(errorCode, details);
    }

    public static ResourceNotFoundException areaNotFound(UUID areaId) {
        return new ResourceNotFoundException(ErrorCode.AREA_NOT_FOUND, "Area not found: " + areaId);
    }

    public static ResourceNotFoundException activityNotFound(UUID activityId) {
        return new ResourceNotFoundException(ErrorCode.ACTIVITY_NOT_FOUND, "Activity not found: " + activityId);
    }

    public static ResourceNotFoundException energyPlanNotFound(UUID planId) {
        return new ResourceNotFoundException(ErrorCode.ENERGY_PLAN_NOT_FOUND, "Energy plan not found: " + planId);
    }

    public static ResourceNotFoundException planProgressNotFound(UUID progressId) {
        return new ResourceNotFoundException(ErrorCode.PLAN_PROGRESS_NOT_FOUND, "Plan progress not found: " + progressId);
    }
}
