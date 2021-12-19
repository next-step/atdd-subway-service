package nextstep.subway.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends CustomExceptionBase {
	public ForbiddenException(String message) {
		super(message, HttpStatus.FORBIDDEN);
	}
}
