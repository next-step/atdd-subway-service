package nextstep.subway.common;

import nextstep.subway.auth.domain.InvalidTokenException;
import nextstep.subway.member.domain.NotFoundMemberException;
import nextstep.subway.path.domain.NotFoundPathException;
import nextstep.subway.station.domain.NotFoundStationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(NotFoundStationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotFoundStationException(NotFoundStationException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NotFoundMemberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotFoundMemberException(NotFoundMemberException e) {
        return e.getMessage();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgsException(DataIntegrityViolationException e) {
        return e.getMessage();
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NotFoundPathException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotFoundPathException(NotFoundPathException e) {
        return e.getMessage();
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleInvalidTokenException(InvalidTokenException e) {
        return e.getMessage();
    }
}
