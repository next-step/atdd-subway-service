package nextstep.subway.fare.exception;

import static java.lang.String.format;

public class LessZeroOperationException extends IllegalArgumentException {
    public LessZeroOperationException(String name) {
        super(format("%s의 연산 결과는 0 미만이 될 수 없습니다.", name));
    }
}
