package nextstep.subway.exception;

public class InvalidTokenException extends BadRequestException {
    public InvalidTokenException() {
        super("token invalid");
    }
}
