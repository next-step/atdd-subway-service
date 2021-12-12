package nextstep.subway.domain.path.exception;

import nextstep.subway.global.error.ErrorCode;
import nextstep.subway.global.exception.BusinessException;

public class SameDepartureAndArrivalStationException extends BusinessException {

    public SameDepartureAndArrivalStationException(final String message) {
        super(message, ErrorCode.SAME_DEPARTURE_AND_ARRIVAL_STATION);
    }
}
