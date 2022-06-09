package nextstep.subway.exception;

public class AlreadyExistException extends IllegalArgumentException {

    public AlreadyExistException() {
        super();
    }

    public AlreadyExistException(String s) {
        super(s);
    }
}
