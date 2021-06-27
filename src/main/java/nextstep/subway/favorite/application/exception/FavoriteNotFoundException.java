package nextstep.subway.favorite.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FavoriteNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FavoriteNotFoundException(){
    }

    public FavoriteNotFoundException(String message) {
        super(message);
    }
}
