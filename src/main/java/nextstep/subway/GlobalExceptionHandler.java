package nextstep.subway;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.exceptions.NotValidTokenException;
import nextstep.subway.exceptions.FavoriteNotExistException;
import nextstep.subway.exceptions.MemberNotExistException;
import nextstep.subway.exceptions.SourceAndTargetSameException;
import nextstep.subway.exceptions.SourceNotConnectedWithTargetException;
import nextstep.subway.exceptions.StationNotExistException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({DataIntegrityViolationException.class, SourceAndTargetSameException.class,
            SourceNotConnectedWithTargetException.class,
            StationNotExistException.class,
            MemberNotExistException.class,
            FavoriteNotExistException.class})
    public ResponseEntity handleBadRequestException(final Exception e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({AuthorizationException.class, NotValidTokenException.class})
    public ResponseEntity handleUnauthorizedException(final Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
