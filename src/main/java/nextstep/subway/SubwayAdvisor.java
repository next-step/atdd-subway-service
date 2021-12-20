package nextstep.subway;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.auth.exception.AuthorizationException;
import nextstep.subway.auth.exception.AuthorizationExtractorException;
import nextstep.subway.exception.domain.ErrorMessage;
import nextstep.subway.member.exception.MemberNotFoundException;
import nextstep.subway.station.exception.StationNotFoundException;

@RestControllerAdvice(basePackages = "nextstep.subway")
public class SubwayAdvisor {

    public static final String ERROR_DEFAULT_MESSAGE = "예기치 못한 오류가 발생했습니다.";

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleMemberNotFoundException(MemberNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.of(e));
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleStationNotFoundException(StationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorMessage.of(e));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgsException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(ErrorMessage.of(e));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().body(ErrorMessage.of(e));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorMessage> handleAuthorizationExceptionException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorMessage.of(e));
    }

    @ExceptionHandler(AuthorizationExtractorException.class)
    public ResponseEntity<ErrorMessage> handleAuthorizationExtractorException(AuthorizationExtractorException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorMessage.of(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleDefaultException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.of(ERROR_DEFAULT_MESSAGE));
    }

}
