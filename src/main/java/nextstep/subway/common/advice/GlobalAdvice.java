package nextstep.subway.common.advice;

import nextstep.subway.auth.exception.AuthorizationException;
import nextstep.subway.favorite.exception.FavoriteException;
import nextstep.subway.line.exception.LineException;
import nextstep.subway.member.exception.MemberException;
import nextstep.subway.path.exception.PathException;
import nextstep.subway.station.exception.StationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalAdvice {
    @ExceptionHandler(StationException.class)
    public ResponseEntity<Void> stationException(final StationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(LineException.class)
    public ResponseEntity<Void> lineException(final LineException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(PathException.class)
    public ResponseEntity<Void> pathException(final PathException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<Void> memberException(final MemberException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(FavoriteException.class)
    public ResponseEntity<Void> favoriteException(final FavoriteException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Void> authException(AuthorizationException e) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> exception(Exception e) {
        return ResponseEntity.badRequest().build();
    }

}
