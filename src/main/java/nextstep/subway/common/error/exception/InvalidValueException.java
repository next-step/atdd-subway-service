package nextstep.subway.common.error.exception;

import nextstep.subway.common.error.ErrorCode;

public class InvalidValueException extends BusinessException {

    public InvalidValueException(final String message) {
        super(message, ErrorCode.INVALID_INPUT_VALUE);
    }

    public InvalidValueException(final ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidValueException(final String message, final ErrorCode errorCode) {
        super(message, errorCode);
    }
}
