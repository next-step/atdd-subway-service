package nextstep.subway.exception;

import nextstep.subway.exception.dto.ErrorResponse;
import nextstep.subway.favorite.exception.FavoriteException;
import nextstep.subway.path.exception.PathFindException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(PathFindException.class)
    public ResponseEntity<ErrorResponse> handlePathFindException(PathFindException pathFindException) {
        ErrorResponse errorResponse = new ErrorResponse(pathFindException.getMessage());

        return ResponseEntity.badRequest()
                .body(errorResponse);
    }

    @ExceptionHandler(FavoriteException.class)
    public ResponseEntity<ErrorResponse> handleFavoriteException(FavoriteException favoriteException) {
        ErrorResponse errorResponse = new ErrorResponse(favoriteException.getMessage());

        return ResponseEntity.badRequest()
                .body(errorResponse);
    }
}
