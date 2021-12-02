package nextstep.subway.common.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.common.exception.NotRegistedMemberException;
import nextstep.subway.common.exception.UnauthoriedRequestException;

@ControllerAdvice
@RestController
public class ControllerExceptionHandler {
    @ExceptionHandler(UnauthoriedRequestException.class)
    public ResponseEntity<Void> handle(UnauthoriedRequestException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(NotRegistedMemberException.class)
    public ResponseEntity<Void> handle(NotRegistedMemberException ex) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
