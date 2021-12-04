package nextstep.subway.common.exception;

public class CannotRemoveException extends RuntimeException {
    public static final String CANNOT_REMOVE_EXCEPTION_STATEMENT = "%s 를(을) 제거할 수 없습니다.";

    public CannotRemoveException(String target) {
        super(String.format(CANNOT_REMOVE_EXCEPTION_STATEMENT, target));
    }
}
