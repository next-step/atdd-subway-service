package nextstep.subway.station.exception;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;

public class StationNotFoundException extends EntityNotFoundException {
    public StationNotFoundException() {
    }

    public StationNotFoundException(String message) {
        super(message);
    }

    public StationNotFoundException(Long id) {
        super(format("%d인 역을 찾을 수 없습니다.", id));
    }
}
