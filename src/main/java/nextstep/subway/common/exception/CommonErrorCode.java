package nextstep.subway.common.exception;

public enum CommonErrorCode implements ErrorCode {
    DATABASE_CONSTRAINT_VIOLATION("고유값이 이미 존재합니다."),
    NOT_EMPTY("빈 값을 입력 할 수 없습니다."),
    ;

    private final String errorMessage;

    CommonErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }
}
