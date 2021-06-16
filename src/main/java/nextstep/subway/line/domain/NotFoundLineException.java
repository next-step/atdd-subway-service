package nextstep.subway.line.domain;

public class NotFoundLineException extends RuntimeException {

    private static final long serialVersionUID = -1201222924578818704L;

    public NotFoundLineException() {
    }

    public NotFoundLineException(String message) {
        super(message);
    }
}
