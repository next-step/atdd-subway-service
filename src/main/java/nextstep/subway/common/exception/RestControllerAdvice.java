package nextstep.subway.common.exception;

import javax.persistence.EntityNotFoundException;
import nextstep.subway.auth.application.AuthorizationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> authorizationException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(StringUtils.defaultString(e.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class, RuntimeException.class,
            EntityNotFoundException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ErrorResponse> customException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(StringUtils.defaultString(e.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorEnum.ERROR_MESSAGE_DEFAULT.message());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
