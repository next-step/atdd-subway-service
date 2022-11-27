package nextstep.subway.exception;

public class EmptyStationNameException extends RuntimeException {
    public EmptyStationNameException(String message) {
        super(message);
    }
}
