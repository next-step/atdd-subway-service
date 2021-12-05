package nextstep.subway.common.exception;

/**
 * 데이터베이스 조회시 정보를 찾지 못할 때 throw 되는 예외입니다.
 */
public class NotFoundException extends DatabaseException {

    private final ErrorCode errorCode;


    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public static NotFoundException of(ErrorCode errorCode) {
        return new NotFoundException(errorCode);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
