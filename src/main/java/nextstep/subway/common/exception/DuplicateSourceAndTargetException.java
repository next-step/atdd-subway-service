package nextstep.subway.common.exception;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
public class DuplicateSourceAndTargetException extends RuntimeException implements BaseException{
	public static final String ERROR_CODE = "DUPLICATE_SOURCE_AND_TARGET_EXCEPTION";

	private String errorMessage = "출발역과 도착역은 동일할 수 없습니다.";

	public DuplicateSourceAndTargetException() {
		super();
	}

	public DuplicateSourceAndTargetException(String message) {
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
