package nextstep.subway.common;

import java.util.NoSuchElementException;

public class LineNotFoundException extends NoSuchElementException {
    private static final String LINE_NOT_FOUND_ERROR = "노선을 찾을 수 없습니다.";

    public LineNotFoundException() {
        super(LINE_NOT_FOUND_ERROR);
    }

    public LineNotFoundException(final String message) {
        super(message);
    }
}
