package nextstep.subway.common.exception;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
public class NotConnectedLineException extends RuntimeException implements BaseException{
	public static final String ERROR_CODE = "NOT_CONNECTED_LINE_EXCEPTION";

	private String errorMessage = "출발역과 도착역이 연결되어있지 않습니다.";

	public NotConnectedLineException() {
		super();
	}

	public NotConnectedLineException(String message) {
		super(message);
		this.errorMessage = message;
	}

	@Override
	public String getErrorCode() {
		return ERROR_CODE;
	}

	@Override
	public String getErrorMessage() {
		return this.errorMessage;
	}
}
