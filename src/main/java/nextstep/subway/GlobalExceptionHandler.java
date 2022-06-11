package nextstep.subway;

import nextstep.subway.favorite.exception.FavoriteNotFoundException;
import nextstep.subway.path.exception.StationNotConnectException;
import nextstep.subway.path.exception.StationNotFoundException;
import nextstep.subway.path.exception.StationsSameException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({StationsSameException.class, StationNotConnectException.class})
    public ResponseEntity handleBadRequest() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({StationNotFoundException.class, FavoriteNotFoundException.class})
    public ResponseEntity handleNotFound() {
        return ResponseEntity.notFound().build();
    }
}
