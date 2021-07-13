package nextstep.subway.common.exception;

public class NoDataException extends  IllegalArgumentException {
	public static final String NO_DATA_EXCEPTION = "데이터가 존재하지 않습니다.";

	public NoDataException() {
		super(NO_DATA_EXCEPTION);
	}
}
