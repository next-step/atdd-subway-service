package nextstep.subway.common;

public class SectionNotRemovableException extends UnsupportedOperationException {
    private static final String SECTION_NOT_REMOVABLE_ERROR = "구간을 제거할 수 없습니다.";

    public SectionNotRemovableException() {
        super(SECTION_NOT_REMOVABLE_ERROR);
    }

    public SectionNotRemovableException(final String message) {
        super(message);
    }
}
