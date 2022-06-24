package nextstep.subway.path.exception;

import nextstep.subway.common.ErrorMessage;
import nextstep.subway.common.exception.SubwayException;
import org.springframework.http.HttpStatus;

public class NotConnectedException extends SubwayException {
    public NotConnectedException(ErrorMessage message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
