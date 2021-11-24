package nextstep.subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("nextstep.subway")
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        e.printStackTrace();
        ErrorResponse response = ErrorResponse.build(e.getMessage());
        return new ResponseEntity<>(response, ErrorCode.DEFAULT_ERROR.getHttpStatus());
    }

    @ExceptionHandler(LineException.class)
    public ResponseEntity<ErrorResponse> handleLineException(LineException e) {
        e.printStackTrace();
        ErrorResponse response = ErrorResponse.build(e.getErrorCode(), e.getMessage());
        return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(SectionException.class)
    public ResponseEntity<ErrorResponse> sectionException(SectionException e) {
        e.printStackTrace();
        ErrorResponse response = ErrorResponse.build(e.getErrorCode(), e.getMessage());
        return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());

    }

}
