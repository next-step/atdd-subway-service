package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class StationNotFoundException extends RuntimeException {
    public StationNotFoundException() {
        super("존재하지 않는 지하철 역입니다.");
    }
}
