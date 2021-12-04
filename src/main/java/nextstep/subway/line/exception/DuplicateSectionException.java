package nextstep.subway.line.exception;

public class DuplicateSectionException extends IllegalArgumentException {

    private static final String DEFAULT_MESSAGE = "이미 등록된 구간 입니다.";

    public DuplicateSectionException() {
        super(DEFAULT_MESSAGE);
    }
}
