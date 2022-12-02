package nextstep.subway.exception;

public class StationNotFoundException extends InternalServerException {
    public StationNotFoundException(String message) {
        super(message);
    }
}
