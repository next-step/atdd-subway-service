package nextstep.subway.common.exception;

public class InvalidPathException extends  IllegalArgumentException {
	public static final String INVALID_PATH_EXCEPTION = "경로가 존재하지않습니다.";

	public InvalidPathException() {
		super(INVALID_PATH_EXCEPTION);
	}
}
