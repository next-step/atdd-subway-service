package nextstep.subway.station.exception;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StationNotFoundException extends EntityNotFoundException {
    public StationNotFoundException() {
    }

    public StationNotFoundException(String message) {
        super(message);
    }
}
