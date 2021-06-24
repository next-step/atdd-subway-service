package nextstep.subway.favorite.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.exception.dto.ErrorResponse;
import nextstep.subway.exception.favorite.NotFoundAnyThingException;
import nextstep.subway.exception.favorite.SameStationException;

@RestControllerAdvice
public class FavoriteControllerAdvice {

    @ExceptionHandler(NotFoundAnyThingException.class)
    public ResponseEntity<ErrorResponse> notFoundAnyThingException(NotFoundAnyThingException e) {
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(SameStationException.class)
    public ResponseEntity<ErrorResponse> sameStationException(SameStationException e) {
        return getBadResponse(e.getMessage());
    }

    private ResponseEntity<ErrorResponse> getBadResponse(String message) {
        ErrorResponse errResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
        return ResponseEntity.badRequest().body(errResponse);
    }

}
