package nextstep.subway.line.application;

import nextstep.subway.exception.SubwayException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RemoveLineException extends SubwayException {
    public RemoveLineException(Object arg) {
        super(arg);
    }
}
