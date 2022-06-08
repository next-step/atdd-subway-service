package nextstep.subway.handler;

import nextstep.subway.auth.application.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, AuthorizationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void badRequest(Exception e) {
        logger.error("Bad Request ", e);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void uncatchedExpection(Exception e) {
        logger.error("Uncatched ", e);
    }
}
