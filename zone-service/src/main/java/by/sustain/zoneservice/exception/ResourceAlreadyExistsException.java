package by.sustain.zoneservice.exception;

public class ResourceAlreadyExistsException extends ApiException {

    public ResourceAlreadyExistsException(ErrorCode errorCode, String details) {
        super(errorCode, details);
    }

    public static ResourceAlreadyExistsException areaAlreadyExists(String name) {
        return new ResourceAlreadyExistsException(
                ErrorCode.AREA_ALREADY_EXISTS,
                "Area with name '" + name + "' already exists"
        );
    }

    public static ResourceAlreadyExistsException activityAlreadyExists(String name, String areaName) {
        return new ResourceAlreadyExistsException(
                ErrorCode.ACTIVITY_ALREADY_EXISTS,
                "Activity with name '" + name + "' already exists in area '" + areaName + "'"
        );
    }
}
