package nextstep.subway.common;

import nextstep.subway.auth.exception.UnAuthorizedException;
import nextstep.subway.path.exception.NotConnectedException;
import nextstep.subway.path.exception.SameSourceAndTargetException;
import nextstep.subway.station.exception.StationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SameSourceAndTargetException.class)
    public ErrorResponse handleSameSourceAndTargetException() {
        return ErrorResponse.of(ErrorMessage.SAME_SOURCE_AND_TARGET.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotConnectedException.class)
    public ErrorResponse handleNotConnectedException() {
        return ErrorResponse.of(ErrorMessage.NOT_CONNECTED.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(StationNotFoundException.class)
    public ErrorResponse handleStationNotFoundException() {
        return ErrorResponse.of(ErrorMessage.STATION_NOT_FOUND.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnAuthorizedException.class)
    public ErrorResponse handleUnAuthorizedException() {
        return ErrorResponse.of(ErrorMessage.UN_AUTHORIZED.getMessage());
    }
}
