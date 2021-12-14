package nextstep.subway.line.exception;

import nextstep.subway.common.error.ErrorCode;
import nextstep.subway.common.error.exception.InvalidValueException;

public class InvalidDistanceException extends InvalidValueException {

    public InvalidDistanceException() {
        super(ErrorCode.INVALID_DISTANCE_VALUE);
    }
}
