package nextstep.subway.common.exception;

import nextstep.subway.common.CustomException;
import org.springframework.http.HttpStatus;

public class RegisterNotAllIncludeException extends CustomException {
    private static final String NOT_REGISTER_NOT_ALL_INCLUDE = "상행, 하행 역 모두가 포함되지 않아서 등록할 수 없습니다.";

    public RegisterNotAllIncludeException() {
        super(HttpStatus.BAD_REQUEST, NOT_REGISTER_NOT_ALL_INCLUDE);
    }
}
