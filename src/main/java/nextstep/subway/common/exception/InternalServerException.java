package nextstep.subway.common.exception;

import org.springframework.http.HttpStatus;

public class InternalServerException extends CustomExceptionBase {
	public InternalServerException(String message) {
		super(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
