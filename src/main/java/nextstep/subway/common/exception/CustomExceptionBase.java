package nextstep.subway.common.exception;

import org.springframework.http.HttpStatus;

public class CustomExceptionBase extends RuntimeException {
	private final HttpStatus status;
	private String additionalMessage = "";

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

	public void withMsg(String additionalMessage) {
		this.additionalMessage = additionalMessage;
	}

	@Override
	public String toString() {
		return super.toString() + "\n"
			+ additionalMessage;
	}
}
