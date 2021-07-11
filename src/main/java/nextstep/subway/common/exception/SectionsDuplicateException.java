package nextstep.subway.common.exception;

public class SectionsDuplicateException extends  IllegalArgumentException {
	public static final String DUPLICATE_EXCEPTION = "이미 등록된 구간 입니다.";

	public SectionsDuplicateException() {
		super(DUPLICATE_EXCEPTION);
	}
}
