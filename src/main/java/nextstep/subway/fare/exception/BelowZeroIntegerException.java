package nextstep.subway.fare.exception;

import static java.lang.String.format;

public class BelowZeroIntegerException extends IllegalArgumentException {
    public BelowZeroIntegerException(String name) {
        super(format("%s은(는) 0 이하가 될 수 없습니다.", name));
    }
}
