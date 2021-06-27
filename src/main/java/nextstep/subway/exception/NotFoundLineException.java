package nextstep.subway.exception;

public class NotFoundLineException extends SubwayCommonException {

    public NotFoundLineException() {
    }

    public NotFoundLineException(String s) {
        super(s);
    }

    public NotFoundLineException(String s, Long id) {
        super(String.format(s, id));
    }
}
