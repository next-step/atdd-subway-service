package nextstep.subway.exception;

public class FareValidException extends BadRequestException {
    public FareValidException(String message) {
        super(message);
    }
}
