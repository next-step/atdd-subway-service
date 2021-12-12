package nextstep.subway.station.exception;

import nextstep.subway.common.error.ErrorCode;
import nextstep.subway.common.error.exception.EntityNotFoundException;

public class StationNotFoundException extends EntityNotFoundException {

    public StationNotFoundException() {
       super(ErrorCode.STATION_NOT_FOUND);
    }
}
