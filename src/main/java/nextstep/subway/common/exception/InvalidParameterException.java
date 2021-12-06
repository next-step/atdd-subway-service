package nextstep.subway.common.exception;

/**
 * 유효하지 않은 매개 변수가 메소드에 전달 될 때 발생합니다.
 */
public class InvalidParameterException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidParameterException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

    public static InvalidParameterException of(ErrorCode errorCode) {
        return new InvalidParameterException(errorCode);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
