package nextstep.subway.exception;

public class InvalidSectionException extends BadRequestException {
    public InvalidSectionException(String message) {
        super(message);
    }
}
