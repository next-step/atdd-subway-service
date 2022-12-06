package nextstep.subway.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

	@ExceptionHandler({DuplicateDataException.class, InvalidDataException.class, NotFoundException.class})
	public ResponseEntity<String> handleDuplicateAndInvalidDataException(RuntimeException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

}
