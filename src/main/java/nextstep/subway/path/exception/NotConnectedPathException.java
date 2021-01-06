package nextstep.subway.path.exception;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-06
 */
public class NotConnectedPathException extends RuntimeException {

    public NotConnectedPathException() {
    }

    public NotConnectedPathException(String message) {
        super(message);
    }
}
