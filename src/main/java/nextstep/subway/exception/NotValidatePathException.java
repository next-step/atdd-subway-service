package nextstep.subway.exception;

public class NotValidatePathException extends RuntimeException {

    public static final String NOT_VALIDATE_PATH_EXCEPTION_MESSAGE = "출발역과 도착역이 올바르지 않습니다.";

    public NotValidatePathException() {
        super(NOT_VALIDATE_PATH_EXCEPTION_MESSAGE);
    }
}
