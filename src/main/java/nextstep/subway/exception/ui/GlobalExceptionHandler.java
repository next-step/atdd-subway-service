package nextstep.subway.exception.ui;

import nextstep.subway.exception.CustomException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<Void> handleCustomException(CustomException e) {
		return ResponseEntity.status(e.getStatus()).build();
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Void> handleCommonException(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
