package nextstep.subway.common;

import java.util.NoSuchElementException;

public class CannotSectionAddException extends NoSuchElementException {
    private static final String CANNOT_SECTION_ADD_ERROR = "등록할 수 없는 구간 입니다.";

    public CannotSectionAddException() {
        super(CANNOT_SECTION_ADD_ERROR);
    }

    public CannotSectionAddException(final String message) {
        super(message);
    }
}
