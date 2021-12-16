package nextstep.subway.common.exception;

import nextstep.subway.common.CustomException;
import org.springframework.http.HttpStatus;

public class RegisterAllIncludeException extends CustomException {
    private static final String NOT_REGISTER_ALL_INCLUDE = "상행, 하행 역 모두가 포함되어 있어 등록할 수 없습니다.";

    public RegisterAllIncludeException() {
        super(HttpStatus.BAD_REQUEST, NOT_REGISTER_ALL_INCLUDE);
    }
}
