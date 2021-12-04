package nextstep.subway.common.exception;

public class NotFoundException extends RuntimeException {
    public static final String NOT_FOUND_EXCEPTION_STATEMENT = "%s 를(을) 찾을 수 없습니다.";

    public NotFoundException(String target) {
        super(String.format(NOT_FOUND_EXCEPTION_STATEMENT, target));
    }
}
