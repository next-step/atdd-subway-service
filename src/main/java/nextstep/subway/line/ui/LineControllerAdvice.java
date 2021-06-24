package nextstep.subway.line.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.exception.dto.ErrorResponse;
import nextstep.subway.exception.line.LineAlreadyExistException;
import nextstep.subway.exception.line.NotFoundSectionException;
import nextstep.subway.exception.line.NotFoundStationsException;

@RestControllerAdvice
public class LineControllerAdvice {

    @ExceptionHandler(LineAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> lineAlreadyExistException(LineAlreadyExistException e) {
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(NotFoundSectionException.class)
    public ResponseEntity<ErrorResponse> notFoundLineException(NotFoundSectionException e) {
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(NotFoundStationsException.class)
    public ResponseEntity<ErrorResponse> notFoundStationsException(NotFoundStationsException e) {
        return getBadResponse(e.getMessage());
    }

    private ResponseEntity<ErrorResponse> getBadResponse(String message) {
        ErrorResponse errResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
        return ResponseEntity.badRequest().body(errResponse);
    }

}
