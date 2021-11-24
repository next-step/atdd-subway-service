package nextstep.subway.exception;

public class LineNotFoundException extends LineException {
    public LineNotFoundException() {
        super();
    }

    public LineNotFoundException(String message) {
        super(message);
    }
}
