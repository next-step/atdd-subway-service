package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StationNotIncludedException extends RuntimeException {
    public StationNotIncludedException() {
        super("역이 그래프에 포함되지 않았습니다.");
    }
}
