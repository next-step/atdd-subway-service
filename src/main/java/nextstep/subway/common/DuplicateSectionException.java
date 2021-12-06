package nextstep.subway.common;

public class DuplicateSectionException extends UnsupportedOperationException {
    private static final String DUPLICATE_SECTION_ERROR = "이미 등록된 구간 입니다.";

    public DuplicateSectionException() {
        super(DUPLICATE_SECTION_ERROR);
    }

    public DuplicateSectionException(final String message) {
        super(message);
    }
}
