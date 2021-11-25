package nextstep.subway.exception;

import nextstep.subway.exception.error.ErrorCode;

public class SectionException extends SubwayException {
    private final ErrorCode errorCode;

    public SectionException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public SectionException(ErrorCode errorCode, String message) {
        super(errorCode, message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
