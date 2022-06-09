package nextstep.subway.exception;

public class NotExistException extends IllegalArgumentException {

    public NotExistException() {
        super();
    }

    public NotExistException(String s) {
        super(s);
    }
}
