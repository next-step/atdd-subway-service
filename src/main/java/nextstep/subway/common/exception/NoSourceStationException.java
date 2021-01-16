package nextstep.subway.common.exception;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
public class NoSourceStationException extends BaseException {
	public static String ERROR_CODE = "NO_SOURCE_STATION_EXCEPTION";
	public static String errorMessage = "출발역이 존재하지 않습니다.";

	public NoSourceStationException() {
	}

	public String getErrorCode() {
		return ERROR_CODE;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}

