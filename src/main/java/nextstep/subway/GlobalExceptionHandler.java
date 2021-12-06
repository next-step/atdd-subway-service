package nextstep.subway;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import nextstep.subway.path.domain.CanNotFindPathException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CanNotFindPathException.class)
	public ResponseEntity<Void> handleCanNotFindPathException(CanNotFindPathException e) {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Void> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
		return ResponseEntity.badRequest().build();
	}
}
