package nextstep.subway.common.handler;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.common.dto.ErrorResponse;
import nextstep.subway.path.exception.DuplicatePathException;
import nextstep.subway.path.exception.NotConnectedPathException;
import nextstep.subway.path.ui.PathController;

@RestControllerAdvice(basePackageClasses = PathController.class)
public class PathExceptionHandler {

	@ExceptionHandler(DuplicatePathException.class)
	public ResponseEntity<ErrorResponse> handleDuplicatePathException(DuplicatePathException e) {
		return ResponseEntity.badRequest().body(ErrorResponse.of(BAD_REQUEST.value(), e.getMessage()));
	}

	@ExceptionHandler(NotConnectedPathException.class)
	public ResponseEntity<ErrorResponse> handleRegisteredSectionException(NotConnectedPathException e) {
		return ResponseEntity.badRequest().body(ErrorResponse.of(BAD_REQUEST.value(), NotConnectedPathException.MESSAGE));
	}
}
