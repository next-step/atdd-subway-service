package nextstep.subway.member.advice;

import nextstep.subway.auth.exception.AuthorizationException;
import nextstep.subway.member.exception.MemberException;
import nextstep.subway.member.ui.MemberController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {MemberController.class})
public class MemberAdvice {
    @ExceptionHandler(MemberException.class)
    public ResponseEntity<Void> memberException(MemberException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Void> authorizationException(AuthorizationException e) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> exception(Exception e) {
        return ResponseEntity.badRequest().build();
    }
}
