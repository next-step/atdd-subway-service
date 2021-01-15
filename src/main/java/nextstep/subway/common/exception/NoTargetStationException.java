package nextstep.subway.common.exception;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
public class NoTargetStationException extends RuntimeException implements BaseException{
	public static final String ERROR_CODE = "NO_TARGET_STATION_EXCEPTION";

	private String errorMessage = "도착역이 존재하지 않습니다.";

	public NoTargetStationException() {
		super();
	}

	public NoTargetStationException(String message) {
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
