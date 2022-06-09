package nextstep.subway.station.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundStationException extends RuntimeException {

    public NotFoundStationException() {
        super("역을 찾을 수 없습니다.");
    }
}
