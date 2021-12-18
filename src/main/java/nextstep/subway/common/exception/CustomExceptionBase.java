package nextstep.subway.common.exception;

import org.springframework.http.HttpStatus;

public class CustomExceptionBase extends RuntimeException {
	protected final HttpStatus status;

	public CustomExceptionBase(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
