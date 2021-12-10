package nextstep.subway.domain.path.exception;

import nextstep.subway.global.error.ErrorCode;
import nextstep.subway.global.exception.BusinessException;

public class StationNotFoundException extends BusinessException {

    public StationNotFoundException(final String message) {
        super(message, ErrorCode.STATION_NOT_FOUND);
    }
}
