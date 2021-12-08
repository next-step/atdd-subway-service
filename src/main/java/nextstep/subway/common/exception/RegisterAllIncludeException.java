package nextstep.subway.common.exception;

public class RegisterAllIncludeException extends RuntimeException {

    private static final String NOT_REGISTER_ALL_INCLUDE = "상행, 하행 역 모두가 포함되어 있어 등록할 수 없습니다.";

    public RegisterAllIncludeException() {
        super(NOT_REGISTER_ALL_INCLUDE);
    }
}
