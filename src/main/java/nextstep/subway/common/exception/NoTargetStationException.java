package nextstep.subway.common.exception;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
public class NoTargetStationException extends BaseException {
	public static final String ERROR_CODE = "NO_TARGET_STATION_EXCEPTION";

	public static String errorMessage = "도착역이 존재하지 않습니다.";

	public NoTargetStationException(){
	}

	public String getErrorCode() {
		return ERROR_CODE;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}