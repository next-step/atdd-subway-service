package nextstep.subway.common.exception;

import nextstep.subway.common.ErrorMessage;
import org.springframework.http.HttpStatus;

public class BadRequestException extends SubwayException{
    public BadRequestException(ErrorMessage message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
