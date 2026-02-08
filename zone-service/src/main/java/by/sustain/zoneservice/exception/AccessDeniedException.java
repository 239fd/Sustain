package by.sustain.zoneservice.exception;

import java.util.UUID;

public class AccessDeniedException extends ApiException {

    public AccessDeniedException(String details) {
        super(ErrorCode.ACCESS_DENIED, details);
    }

    public static AccessDeniedException areaAccessDenied(UUID areaId) {
        return new AccessDeniedException("You don't have access to area: " + areaId);
    }

    public static AccessDeniedException activityAccessDenied(UUID activityId) {
        return new AccessDeniedException("You don't have access to activity: " + activityId);
    }

    public static AccessDeniedException energyPlanAccessDenied(UUID planId) {
        return new AccessDeniedException("You don't have access to energy plan: " + planId);
    }

    public static AccessDeniedException cannotModifyOthersResource() {
        return new AccessDeniedException("You cannot modify resources belonging to other users");
    }
}
