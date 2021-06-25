package nextstep.subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import nextstep.subway.auth.domain.AuthorizationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<Void> handleAuthorizationException(AuthorizationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
}
