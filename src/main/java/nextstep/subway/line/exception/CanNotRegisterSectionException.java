package nextstep.subway.line.exception;

public class CanNotRegisterSectionException extends IllegalArgumentException {

    private static final String DEFAULT_MESSAGE = "등록할 수 없는 구간 입니다.";

    public CanNotRegisterSectionException() {
        super(DEFAULT_MESSAGE);
    }
}
