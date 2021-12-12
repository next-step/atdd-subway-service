package nextstep.subway.common;

public class InvalidException extends RuntimeException {
    public InvalidException() {
    }

    public InvalidException(String message) {
        super(message);
    }
}
