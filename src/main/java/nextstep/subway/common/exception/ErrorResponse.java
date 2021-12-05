package nextstep.subway.common.exception;

public class ErrorResponse {

    String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.message = errorCode.getErrorMessage();
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    public String getMessage() {
        return message;
    }
}
