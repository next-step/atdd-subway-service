package nextstep.subway.common.exception;

import nextstep.subway.common.CustomException;

public class NoRequiredValueException extends CustomException {
    private static final String NO_REQUIRED_VALUE = "필수값이 입력되지 않았습니다.";

    public NoRequiredValueException() {
        super(NO_REQUIRED_VALUE);
    }
}
