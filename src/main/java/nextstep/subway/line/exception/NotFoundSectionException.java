package nextstep.subway.line.exception;

import static java.lang.String.format;

public class NotFoundSectionException extends RuntimeException {

    public NotFoundSectionException(String stationName) {
        super(format("'%s' 을 찾을 수 없습니다", stationName));
    }
}
