package by.sustain.zoneservice.exception;

public class InvalidInputDataException extends ApiException {

    public InvalidInputDataException(String details) {
        super(ErrorCode.INVALID_INPUT_DATA, details);
    }

    public static InvalidInputDataException emptyName() {
        return new InvalidInputDataException("Name cannot be empty");
    }

    public static InvalidInputDataException invalidEnergy(String value) {
        return new InvalidInputDataException("Invalid energy level: " + value + ". Allowed values: HIGH, MEDIUM, LOW");
    }

    public static InvalidInputDataException invalidColor(String value) {
        return new InvalidInputDataException("Invalid HEX color format: " + value + ". Expected format: #RRGGBB");
    }

    public static InvalidInputDataException invalidUuid(String field, String value) {
        return new InvalidInputDataException("Invalid UUID format for " + field + ": " + value);
    }

    public static InvalidInputDataException invalidInterval(String field, String value) {
        return new InvalidInputDataException("Invalid interval for " + field + ": " + value + ". Allowed values: YEAR, MONTH, WEEK, DAY");
    }

    public static InvalidInputDataException invalidFrequency() {
        return new InvalidInputDataException("Frequency must be greater than 0");
    }
}
