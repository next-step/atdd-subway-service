package nextstep.subway.station.exception;

import static nextstep.subway.common.Message.MESSAGE_STATION_NOT_FOUND;

import nextstep.subway.common.NoResultDataException;

public class StationNotFoundException extends NoResultDataException {

    public StationNotFoundException() {
        super(MESSAGE_STATION_NOT_FOUND.getMessage());
    }

    public StationNotFoundException(String message) {
        super(message);
    }
}
