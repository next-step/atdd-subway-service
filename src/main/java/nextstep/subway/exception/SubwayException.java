package nextstep.subway.exception;

import nextstep.subway.exception.error.ErrorCode;

public class SubwayException extends RuntimeException {
    private final ErrorCode errorCode;

    public SubwayException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public SubwayException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
