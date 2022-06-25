package nextstep.subway.auth.advice;

import nextstep.subway.auth.exception.AuthorizationException;
import nextstep.subway.auth.ui.AuthController;
import nextstep.subway.member.exception.MemberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {AuthController.class})
public class AuthAdvice {
    @ExceptionHandler(MemberException.class)
    public ResponseEntity<Void> memberException(MemberException e) {
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
