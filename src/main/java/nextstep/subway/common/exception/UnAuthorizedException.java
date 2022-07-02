package nextstep.subway.common.exception;

import nextstep.subway.common.ErrorMessage;
import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends SubwayException {
    public UnAuthorizedException(ErrorMessage message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
