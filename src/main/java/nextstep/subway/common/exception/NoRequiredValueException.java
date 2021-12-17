package nextstep.subway.common.exception;

import nextstep.subway.common.CustomException;
import org.springframework.http.HttpStatus;

public class NoRequiredValueException extends CustomException {
    private static final String NO_REQUIRED_VALUE = "필수값이 입력되지 않았습니다.";

    public NoRequiredValueException() {
        super(HttpStatus.BAD_REQUEST, NO_REQUIRED_VALUE);
    }
}
