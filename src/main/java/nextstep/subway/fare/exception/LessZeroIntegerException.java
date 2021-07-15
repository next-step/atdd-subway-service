package nextstep.subway.fare.exception;

import static java.lang.String.format;

public class LessZeroIntegerException extends IllegalArgumentException{
    public LessZeroIntegerException(String name) {
        super(format("%s은(는) 0 미만이 될 수 없습니다.", name));
    }
}
