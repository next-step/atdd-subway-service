package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FavoriteDeleteException extends RuntimeException {
    public FavoriteDeleteException(String message) {
        super(message);
    }
}
