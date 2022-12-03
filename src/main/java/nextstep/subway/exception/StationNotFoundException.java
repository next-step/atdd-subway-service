package nextstep.subway.exception;

public class StationNotFoundException extends DomainException {
    public StationNotFoundException(String message) {
        super(message);
    }
}
