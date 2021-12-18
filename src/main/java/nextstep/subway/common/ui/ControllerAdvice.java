package nextstep.subway.common.ui;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.common.exception.CustomExceptionBase;

@RestControllerAdvice
public class ControllerAdvice {
	@ExceptionHandler(CustomExceptionBase.class)
	public ResponseEntity<String> errorHandler(CustomExceptionBase e) {
		return new ResponseEntity<>(e.getMessage(), e.getStatus());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
		return ResponseEntity.badRequest().build();
	}
}
