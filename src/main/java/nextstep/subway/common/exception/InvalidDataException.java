package nextstep.subway.common.exception;

public class InvalidDataException extends RuntimeException {

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Exception e) {
        super(message, e);
    }
}
