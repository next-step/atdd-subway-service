package nextstep.subway.station.exception;

public class NoSuchStationException extends RuntimeException {

    public NoSuchStationException() {
    }

    public NoSuchStationException(String message) {
        super(message);
    }
}
