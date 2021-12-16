package nextstep.subway.common.exception;

import nextstep.subway.common.CustomException;
import org.springframework.http.HttpStatus;

public class RegisterDistanceException extends CustomException {
    private static final String NOT_REGISTER_SECTION_DISTANCE = "등록할 수 없는 구간입니다.";

    public RegisterDistanceException() {
        super(HttpStatus.BAD_REQUEST, NOT_REGISTER_SECTION_DISTANCE);
    }
}