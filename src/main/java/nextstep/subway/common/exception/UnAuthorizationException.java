package nextstep.subway.common.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizationException extends CustomExceptionBase
{
	public UnAuthorizationException(String message) {
		super(message, HttpStatus.FORBIDDEN);
	}
}
