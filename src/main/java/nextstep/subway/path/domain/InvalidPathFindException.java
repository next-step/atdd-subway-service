package nextstep.subway.path.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPathFindException extends RuntimeException {

    public InvalidPathFindException(String message) {
        super(message);
    }
}
