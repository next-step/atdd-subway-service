package nextstep.subway.path.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PathExceptionHandler {
	private static final String STATION_NOT_CONNECTION = "출발역과 도착역은 연결되어야 합니다.";

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgsException(IllegalArgumentException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<String> handleNullPointerException(NullPointerException e) {
		return new ResponseEntity<>(STATION_NOT_CONNECTION, HttpStatus.BAD_REQUEST);
	}
}
