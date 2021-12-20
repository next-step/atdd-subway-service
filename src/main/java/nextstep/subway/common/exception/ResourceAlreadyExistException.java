package nextstep.subway.common.exception;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistException extends CustomExceptionBase {

	public ResourceAlreadyExistException(String message) {
		super(message, HttpStatus.BAD_REQUEST);
	}
}
