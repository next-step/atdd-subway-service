package nextstep.subway.auth.exception;

public class InValidTokenException extends RuntimeException {
    public InValidTokenException(String message) {
        super(message);
    }
}
