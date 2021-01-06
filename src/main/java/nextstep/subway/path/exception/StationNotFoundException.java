package nextstep.subway.path.exception;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-06
 */
public class StationNotFoundException extends RuntimeException {

    public StationNotFoundException() {
    }

    public StationNotFoundException(String message) {
        super(message);
    }
}
