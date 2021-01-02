package nextstep.subway.favorite.ui;

import nextstep.subway.favorite.application.exceptions.NotMyFavoriteException;
import nextstep.subway.favorite.domain.excpetions.FavoriteCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FavoriteControllerAdvice {
    @ExceptionHandler(FavoriteCreationException.class)
    public ResponseEntity handleFavoriteCreationException(FavoriteCreationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NotMyFavoriteException.class)
    public ResponseEntity handleNotMyFavoriteException(NotMyFavoriteException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
