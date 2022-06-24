package nextstep.subway.common.exception;

import nextstep.subway.common.ErrorMessage;
import org.springframework.http.HttpStatus;

public class NotFoundException extends SubwayException {
    public NotFoundException(ErrorMessage message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
