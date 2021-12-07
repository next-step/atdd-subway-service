package nextstep.subway.line.exception;

public class IllegalLineArgumentException extends IllegalArgumentException {
    public IllegalLineArgumentException(String errorMessage) {
        super(errorMessage);
    }
}
