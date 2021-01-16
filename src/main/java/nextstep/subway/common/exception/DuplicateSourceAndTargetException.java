package nextstep.subway.common.exception;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
public class DuplicateSourceAndTargetException extends BaseException {
	public static String ERROR_CODE = "DUPLICATE_SOURCE_AND_TARGET_EXCEPTION";

	public static String errorMessage = "출발역과 도착역은 동일할 수 없습니다.";

	public DuplicateSourceAndTargetException() {
	}

	@Override
	public String getErrorCode() {
		return ERROR_CODE;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}
