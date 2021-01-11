package nextstep.subway.common;

import org.springframework.dao.DataIntegrityViolationException;
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

	@ExceptionHandler(
		{
			NothingException.class,
			RuntimeException.class,
			IllegalArgumentException.class,
			DataIntegrityViolationException.class
		})
	public ResponseEntity<LineResponse> handleBadRequestException(Exception e) {
		log.error("Bad request Error: " + e.getMessage());
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(InternalException.class)
	public ResponseEntity<?> handleInternalException(InternalException e) {
		log.error("InternalException: " + e.getMessage());
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
