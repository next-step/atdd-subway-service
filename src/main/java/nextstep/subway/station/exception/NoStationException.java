package nextstep.subway.station.exception;

import static nextstep.subway.exception.ExceptionMessage.*;

import nextstep.subway.exception.BadRequestException;

public class NoStationException extends BadRequestException {
    public NoStationException(Long id) {
        super(noStation(id));
    }
}
