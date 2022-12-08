package nextstep.subway.exception;

public class NegativeOverFareException extends BadRequestException {
    public NegativeOverFareException(String message) {
        super(message);
    }
}
