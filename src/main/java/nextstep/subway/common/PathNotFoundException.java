package nextstep.subway.common;

import java.util.NoSuchElementException;

public class PathNotFoundException extends NoSuchElementException {
    private static final String PATH_NOT_FOUND_ERROR = "경로를 찾을 수 없습니다.";

    public PathNotFoundException() {
        super(PATH_NOT_FOUND_ERROR);
    }

    public PathNotFoundException(final String message) {
        super(message);
    }
}
