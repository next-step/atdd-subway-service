package nextstep.subway.error;

import nextstep.subway.auth.application.exception.AuthorizationException;
import nextstep.subway.favorite.application.exception.FavoriteNotFoundException;
import nextstep.subway.line.application.exception.LineNotFoundException;
import nextstep.subway.member.application.exception.DuplicateEmailException;
import nextstep.subway.member.application.exception.MemberNotFoundException;
import nextstep.subway.station.application.exception.StationNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(LineNotFoundException.class)
    public ErrorResponse handleLineNotFoundException(LineNotFoundException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MemberNotFoundException.class)
    public ErrorResponse handleMemberNotFoundException(MemberNotFoundException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(StationNotFoundException.class)
    public ErrorResponse handleStationNotFoundException(StationNotFoundException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FavoriteNotFoundException.class)
    public ErrorResponse handleFavoriteNotFoundException(FavoriteNotFoundException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateEmailException.class)
    public ErrorResponse handleDuplicateEmailException(DuplicateEmailException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthorizationException.class)
    public ErrorResponse handleAuthorizationException(AuthorizationException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse handleIllegalArgsException(DataIntegrityViolationException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Throwable.class)
    public ErrorResponse handleThrowableException(Throwable e) {
        return ErrorResponse.of(e.getMessage());
    }
}
