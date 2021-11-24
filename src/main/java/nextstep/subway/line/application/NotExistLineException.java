package nextstep.subway.line.application;

public class NotExistLineException extends RuntimeException {

    public NotExistLineException() {
    }

    public NotExistLineException(String message) {
        super(message);
    }
}
