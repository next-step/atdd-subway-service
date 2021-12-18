package nextstep.subway.path.exception;

public class NotFoundStationsInLineException extends RuntimeException {
    public NotFoundStationsInLineException(String message) {
        super(message);
    }
}
