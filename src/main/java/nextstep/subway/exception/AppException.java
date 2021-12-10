package nextstep.subway.exception;

import org.apache.logging.log4j.message.ParameterizedMessage;

public class AppException extends RuntimeException {

	private final ErrorCode errorCode;
	private final String description;

	public AppException(ErrorCode errorCode, String descriptionPattern, Object... descArgs) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.description = ParameterizedMessage.format(descriptionPattern, descArgs);
	}

	public ErrorCode getErrorCode() {
		return this.errorCode;
	}

	public String getDescription() {
		return this.description;
	}

}
