package nextstep.subway.favorite.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnableDeleteException extends RuntimeException {
    public UnableDeleteException() {
    }

    public UnableDeleteException(String message) {
        super(message);
    }
}
