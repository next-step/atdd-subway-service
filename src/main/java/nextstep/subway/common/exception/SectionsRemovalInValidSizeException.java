package nextstep.subway.common.exception;

public class SectionsRemovalInValidSizeException extends  IllegalArgumentException {
	public static final String INVALID_SIZE_EXCEPTION = "구간을 제거할 수 있는 최소 크기보다 작습니다.";

	public SectionsRemovalInValidSizeException() {
		super(INVALID_SIZE_EXCEPTION);
	}
}
