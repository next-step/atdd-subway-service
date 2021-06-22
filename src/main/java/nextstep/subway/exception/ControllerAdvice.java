package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import nextstep.subway.exception.dto.ErrorResponse;

public class ControllerAdvice {

    public ResponseEntity<ErrorResponse> getBadResponse(String message) {
        ErrorResponse errResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
        return ResponseEntity.badRequest().body(errResponse);
    }

    public ResponseEntity<ErrorResponse> getSeverErrorResponse(String message) {
        ErrorResponse errResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errResponse);
    }

}
