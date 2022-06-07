package nextstep.subway.exception;

public class AlreadyExistException extends IllegalStateException {

    public AlreadyExistException() {
        super();
    }

    public AlreadyExistException(String s) {
        super(s);
    }
}
