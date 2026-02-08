package by.sustain.zoneservice.exception;

public enum ErrorCode {

    INVALID_INPUT_DATA("INVALID_INPUT_DATA", "Invalid input data"),

    MISSING_AUTH_TOKEN("MISSING_AUTH_TOKEN", "Missing or invalid auth token"),

    ACCESS_DENIED("ACCESS_DENIED", "Access denied"),

    AREA_NOT_FOUND("AREA_NOT_FOUND", "Area not found"),
    ACTIVITY_NOT_FOUND("ACTIVITY_NOT_FOUND", "Activity not found"),
    ENERGY_PLAN_NOT_FOUND("ENERGY_PLAN_NOT_FOUND", "Energy plan not found"),
    PLAN_PROGRESS_NOT_FOUND("PLAN_PROGRESS_NOT_FOUND", "Plan progress not found"),

    AREA_ALREADY_EXISTS("AREA_ALREADY_EXISTS", "Area already exists"),
    ACTIVITY_ALREADY_EXISTS("ACTIVITY_ALREADY_EXISTS", "Activity already exists");

    private final String code;
    private final String defaultMessage;

    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
