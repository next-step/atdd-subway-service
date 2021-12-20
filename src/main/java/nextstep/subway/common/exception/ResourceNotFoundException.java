package nextstep.subway.common.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends CustomExceptionBase {
	public ResourceNotFoundException(String message) {
		super(message, HttpStatus.NOT_FOUND);
	}
}
