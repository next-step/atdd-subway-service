package nextstep.subway.exception;

import nextstep.subway.exception.error.ErrorCode;

public class FavoriteException extends SubwayException {
    private final ErrorCode errorCode;

    public FavoriteException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public FavoriteException(ErrorCode errorCode, String message) {
        super(errorCode, message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
