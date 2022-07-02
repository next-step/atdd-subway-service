package nextstep.subway.path.exception;

import nextstep.subway.common.ErrorMessage;
import nextstep.subway.common.exception.SubwayException;
import org.springframework.http.HttpStatus;

public class SameSourceAndTargetException extends SubwayException {
    public SameSourceAndTargetException(ErrorMessage message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
