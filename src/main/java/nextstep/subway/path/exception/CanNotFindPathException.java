package nextstep.subway.path.exception;

public class CanNotFindPathException extends IllegalArgumentException {
    public CanNotFindPathException(String errorMessage) {
        super(errorMessage);
    }
}
