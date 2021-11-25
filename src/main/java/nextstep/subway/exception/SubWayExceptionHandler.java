package nextstep.subway.exception;

import nextstep.subway.exception.error.ErrorCode;
import nextstep.subway.exception.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("nextstep.subway")
public class SubWayExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        e.printStackTrace();
        ErrorResponse response = ErrorResponse.build(e.getMessage());
        return new ResponseEntity<>(response, ErrorCode.DEFAULT_ERROR.getHttpStatus());
    }

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ErrorResponse> handleSubwayException(SubwayException e) {
        e.printStackTrace();
        ErrorResponse response = ErrorResponse.build(e.getErrorCode(), e.getMessage());
        return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
    }

}
