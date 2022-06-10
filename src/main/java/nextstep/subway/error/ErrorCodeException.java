package nextstep.subway.error;

public class ErrorCodeException extends RuntimeException {
    public ErrorCodeException(ErrorCode code) {
        super(code.message);
    }
}
