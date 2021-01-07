package nextstep.subway.station.application;

import nextstep.subway.exception.SubwayException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StationNotFoundException extends SubwayException {
    public StationNotFoundException(Object arg) {
        super(arg);
    }
}
