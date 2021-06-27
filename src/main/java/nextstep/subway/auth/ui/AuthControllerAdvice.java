package nextstep.subway.auth.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.exception.auth.AuthorizationException;
import nextstep.subway.exception.dto.ErrorResponse;

@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> authorizationException(AuthorizationException e) {
        return getBadResponse(e.getMessage());
    }

    private ResponseEntity<ErrorResponse> getBadResponse(String message) {
        ErrorResponse errResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(errResponse);
    }

}
