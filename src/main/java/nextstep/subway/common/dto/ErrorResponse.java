package nextstep.subway.common.dto;

import nextstep.subway.common.exception.BaseException;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
public class ErrorResponse {
	private String errorCode;
	private String errorMessage;

	protected ErrorResponse() {
	}

	public ErrorResponse(BaseException exception) {
		this.errorCode = exception.getErrorCode();
		this.errorMessage = exception.getErrorMessage();
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
