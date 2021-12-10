package nextstep.subway.line.exception;

public class NotExistLineException extends RuntimeException {

	private static String ERROR_MESSAGE = "존재하지 지하철 노선 입니다.";

	public NotExistLineException() {
		super(ERROR_MESSAGE);
	}
}