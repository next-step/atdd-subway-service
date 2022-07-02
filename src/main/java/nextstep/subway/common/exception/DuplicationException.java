package nextstep.subway.common.exception;

import nextstep.subway.common.ErrorMessage;
import org.springframework.http.HttpStatus;

public class DuplicationException extends SubwayException {
    public DuplicationException(ErrorMessage message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
