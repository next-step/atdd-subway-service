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

    public ErrorCode getCode() {
        return code;
    }

    public int getStatus() {
        return code.getStatus().value();
    }
}
