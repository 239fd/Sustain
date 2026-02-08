package by.sustain.zoneservice.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String details;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.details = null;
    }

    public ApiException(ErrorCode errorCode, String details) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.details = details;
    }

    public ApiException(ErrorCode errorCode, String details, Throwable cause) {
        super(errorCode.getDefaultMessage(), cause);
        this.errorCode = errorCode;
        this.details = details;
    }
}
