package nextstep.subway.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

	@ExceptionHandler({DuplicateDataException.class, InvalidDataException.class, NotFoundException.class})
	public ResponseEntity<String> handleDuplicateAndInvalidDataException(RuntimeException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<String> handleAuthorizationException() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

}
