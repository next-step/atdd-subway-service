package nextstep.subway.common;

import nextstep.subway.favorite.application.exception.FavoritePathNotFoundException;
import nextstep.subway.line.application.exception.InvalidSectionException;
import nextstep.subway.line.application.exception.LineNotFoundException;
import nextstep.subway.line.application.exception.SectionNotFoundException;
import nextstep.subway.member.application.exception.MemberNotFoundException;
import nextstep.subway.path.application.exception.InvalidPathException;
import nextstep.subway.station.application.exception.StationNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handler(DataIntegrityViolationException e) {
        ApiError apiError = new ApiError(BAD_REQUEST, e.getLocalizedMessage(), e.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<ApiError> handler(StationNotFoundException e) {
        ApiError apiError = new ApiError(NOT_FOUND, e.getLocalizedMessage(), e.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(SectionNotFoundException.class)
    public ResponseEntity<ApiError> handler(SectionNotFoundException e) {
        ApiError apiError = new ApiError(NOT_FOUND, e.getLocalizedMessage(), e.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(LineNotFoundException.class)
    public ResponseEntity<ApiError> handler(LineNotFoundException e) {
        ApiError apiError = new ApiError(NOT_FOUND, e.getLocalizedMessage(), e.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ApiError> handler(MemberNotFoundException e) {
        ApiError apiError = new ApiError(NOT_FOUND, e.getLocalizedMessage(), e.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(FavoritePathNotFoundException.class)
    public ResponseEntity<ApiError> handler(FavoritePathNotFoundException e) {
        ApiError apiError = new ApiError(NOT_FOUND, e.getLocalizedMessage(), e.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(InvalidSectionException.class)
    public ResponseEntity<ApiError> handler(InvalidSectionException e) {
        ApiError apiError = new ApiError(BAD_REQUEST, e.getLocalizedMessage(), e.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(InvalidPathException.class)
    public ResponseEntity<ApiError> handler(InvalidPathException e) {
        ApiError apiError = new ApiError(BAD_REQUEST, e.getLocalizedMessage(), e.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}
