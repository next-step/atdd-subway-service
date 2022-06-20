package nextstep.subway.fare.application;

public class InvalidFareException extends IllegalArgumentException {
    public InvalidFareException(String message) {
        super(message);
    }
}
