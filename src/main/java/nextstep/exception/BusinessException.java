package nextstep.exception;

public class BusinessException extends RuntimeException {

    private ErrorCode code;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
