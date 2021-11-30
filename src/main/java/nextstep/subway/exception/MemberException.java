package nextstep.subway.exception;

import nextstep.subway.exception.error.ErrorCode;

public class MemberException extends SubwayException {
    private final ErrorCode errorCode;

    public MemberException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public MemberException(ErrorCode errorCode, String message) {
        super(errorCode, message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
