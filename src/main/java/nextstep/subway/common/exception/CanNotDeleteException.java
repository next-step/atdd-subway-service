package nextstep.subway.common.exception;

import nextstep.subway.common.ErrorMessage;
import org.springframework.http.HttpStatus;

public class CanNotDeleteException extends SubwayException {
    public CanNotDeleteException(ErrorMessage message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
