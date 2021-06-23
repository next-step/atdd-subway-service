package nextstep.subway.exception;

public class NoLoginUserException extends RuntimeException {
    public NoLoginUserException() {
    }

    public NoLoginUserException(String message) {
        super(message);
    }
}
