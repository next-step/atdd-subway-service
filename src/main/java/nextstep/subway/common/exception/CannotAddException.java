package nextstep.subway.common.exception;

public class CannotAddException extends RuntimeException {
    public static final String CANNOT_ADD_EXCEPTION_STATEMENT = "%s 를(을) 추가할 수 없습니다.";

    public CannotAddException(String target) {
        super(String.format(CANNOT_ADD_EXCEPTION_STATEMENT, target));
    }
}
