package nextstep.subway.advice;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.application.FavoriteValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException : ", e);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFoundException(NotFoundException e) {
        log.error("NotFoundException : ", e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException : ", e);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException : ", e);
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleAuthorizationException(AuthorizationException e) {
        log.error("AuthorizationException : ", e);
    }

    @ExceptionHandler(FavoriteValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleFavoriteValidationException(FavoriteValidationException e) {
        log.error("FavoriteValidationException", e);
    }

}
