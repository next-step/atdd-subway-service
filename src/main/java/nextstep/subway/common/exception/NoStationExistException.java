package nextstep.subway.common.exception;

/**
 * @author : byungkyu
 * @date : 2021/01/16
 * @description :
 **/
public class NoStationExistException extends BaseException {
	public static String ERROR_CODE = "NO_STATION_EXCEPTION";
	public static String errorMessage = "지하철 역이 존재하지 않습니다.";

	public NoStationExistException() {
	}

	public String getErrorCode() {
		return ERROR_CODE;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}

