package nextstep.subway.exception;

public class EmptyStationNameException extends DomainException {
    public EmptyStationNameException(String message) {
        super(message);
    }
}
