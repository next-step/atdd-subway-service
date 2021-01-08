package nextstep.subway.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import nextstep.subway.common.exception.NothingException;
import nextstep.subway.line.dto.LineResponse;

@Slf4j
@RestControllerAdvice
public class Advice {

	@ExceptionHandler(NothingException.class)
	public ResponseEntity<LineResponse> handleLineNotFoundException(NothingException e) {
		log.error("Target NotFoundException: " + e.getMessage());
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
		log.error("RuntimeException: " + e.getMessage());
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
		log.error("IllegalArgumentException: " + e.getMessage());
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(InternalException.class)
	public ResponseEntity<?> handleInternalException(InternalException e) {
		log.error("InternalException: " + e.getMessage());
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
