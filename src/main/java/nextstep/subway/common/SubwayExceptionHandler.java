package nextstep.subway.common;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.line.exception.NotExistLineException;
import nextstep.subway.path.exception.NotFoundPathException;
import nextstep.subway.station.exception.NotExistStationException;

@RestControllerAdvice
public class SubwayExceptionHandler {

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity handleDataIntegrityViolationException(DataIntegrityViolationException e) {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(NotFoundPathException.class)
	public ResponseEntity handleNotFoundPathException(NotFoundPathException e) {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(NotExistLineException.class)
	public ResponseEntity handleNotExistLineException(NotExistLineException e) {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(NotExistStationException.class)
	public ResponseEntity handleNotExistStationException(NotExistStationException e) {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity handleIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity handleIllegalStateException(IllegalStateException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
}
