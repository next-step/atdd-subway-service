package nextstep.subway.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Void> handleNotFoundException(NotFoundException e) {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler({DuplicateDataException.class, InvalidDataException.class})
	public ResponseEntity<String> handleDuplicateAndInvalidDataException(RuntimeException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

}
