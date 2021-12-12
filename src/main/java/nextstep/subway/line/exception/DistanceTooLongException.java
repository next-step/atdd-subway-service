package nextstep.subway.line.exception;

import nextstep.subway.common.error.ErrorCode;
import nextstep.subway.common.error.exception.InvalidValueException;

public class DistanceTooLongException extends InvalidValueException {

    public DistanceTooLongException(final String message) {
        super(message, ErrorCode.DISTANCE_TOO_LONG);
    }
}
