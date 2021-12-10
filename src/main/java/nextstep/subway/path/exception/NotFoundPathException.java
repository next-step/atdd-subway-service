package nextstep.subway.path.exception;

public class NotFoundPathException extends RuntimeException {

	private static String ERROR_MESSAGE = "경로를 찾지 못하였습니다.";

	public NotFoundPathException() {
		super(ERROR_MESSAGE);
	}
}