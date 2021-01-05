package nextstep.subway.line.exception;

public class WrongDistanceException extends RuntimeException {

    public WrongDistanceException() {
        super();
    }

    public WrongDistanceException(String message) {
        super(message);
    }
}
