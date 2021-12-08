package nextstep.subway.common.exception;

public class RegisterDistanceException extends RuntimeException {

    private static final String NOT_REGISTER_SECTION_DISTANCE = "등록할 수 없는 구간입니다.";

    public RegisterDistanceException() {
        super(NOT_REGISTER_SECTION_DISTANCE);
    }
}