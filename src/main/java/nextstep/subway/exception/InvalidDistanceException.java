package nextstep.subway.exception;

public class InvalidDistanceException extends BadRequestException {
    public InvalidDistanceException(String message) {
        super(message);
    }
}
