package nextstep.subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.exception.dto.ErrorResponse;
import nextstep.subway.exception.station.NoStation;

@RestControllerAdvice
public class StationControllerAdvice extends ControllerAdvice {

    @ExceptionHandler(NoStation.class)
    public ResponseEntity<ErrorResponse> stationsAlreadyExistException(NoStation e) {
        return getSeverErrorResponse(e.getMessage());
    }

}
