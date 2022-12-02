package nextstep.subway.exception;

public class PathNotFoundException extends InternalServerException {
    public PathNotFoundException(String message) {
        super(message);
    }
}
