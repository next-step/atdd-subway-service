package nextstep.subway.common;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.common.exception.NothingException;

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
	public ResponseEntity<?> handleBadRequestException(Exception exception) {
		log.error("Bad request Error: " + exception.getMessage());
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(InternalException.class)
	public ResponseEntity<?> handleInternalException(InternalException exception) {
		log.error("InternalException: " + exception.getMessage());
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<?> handleAuthorizationException(AuthorizationException exception) {
		log.error("AuthorizationException: " + exception.getMessage());
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
}
