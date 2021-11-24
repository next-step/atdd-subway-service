package nextstep.subway.exception;

import nextstep.subway.exception.error.ErrorCode;

public class LineException extends SubwayException {
    private final ErrorCode errorCode;

    public LineException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public LineException(ErrorCode errorCode, String message) {
        super(errorCode, message);
        this.errorCode = errorCode;
    }
}
