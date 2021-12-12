package nextstep.subway.path.exception;

import nextstep.subway.common.error.ErrorCode;
import nextstep.subway.common.error.exception.InvalidValueException;

public class InvalidPathStationException extends InvalidValueException {
    public InvalidPathStationException(final String message) {
        super(message, ErrorCode.INVALID_PATH_STATION_VALUE);
    }
}
