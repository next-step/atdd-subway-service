package nextstep.subway.line.application;

import nextstep.subway.exception.SubwayException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LineNotFoundException extends SubwayException {
    public LineNotFoundException(Object arg) {
        super(arg);
    }
}
