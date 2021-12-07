package nextstep.subway.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException() {
    }

    public BadRequestException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.getMessage());
    }
}
