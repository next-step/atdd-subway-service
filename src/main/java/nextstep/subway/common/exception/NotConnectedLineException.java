package nextstep.subway.common.exception;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
public class NotConnectedLineException extends BaseException {
	public static String ERROR_CODE = "NOT_CONNECTED_LINE_EXCEPTION";

	public static String ERROR_MESSAGE = "출발역과 도착역이 연결되어있지 않습니다.";

	public NotConnectedLineException() {
	}

	public String getErrorCode() {
		return ERROR_CODE;
	}

	@Override
	public String getErrorMessage() {
		return ERROR_MESSAGE;
	}

}