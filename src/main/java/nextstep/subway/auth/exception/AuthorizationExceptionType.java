package nextstep.subway.auth.exception;

public enum AuthorizationExceptionType {
    EXPIRE_TOKEN("토큰 유효 기간이 만료 되었습니다."),
    INVALID_TOKEN("토큰이 올바르지 않습니다.");

    private final String message;

    AuthorizationExceptionType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "AuthorizationExceptionType{" +
                "message='" + message + '\'' +
                '}';
    }

}
