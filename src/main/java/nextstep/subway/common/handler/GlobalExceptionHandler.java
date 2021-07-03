package nextstep.subway.common.handler;

import static org.springframework.http.HttpStatus.*;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.common.dto.ErrorResponse;
import nextstep.subway.station.exeption.NotFoundStationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgsException(DataIntegrityViolationException e) {
		return ResponseEntity.badRequest().body(ErrorResponse.of(BAD_REQUEST.value(), e.getMessage()));
	}

	@ExceptionHandler(NotFoundStationException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundStationException(NotFoundStationException e) {
		return ResponseEntity.badRequest().body(ErrorResponse.of(BAD_REQUEST.value(), NotFoundStationException.MESSAGE));
	}
}
