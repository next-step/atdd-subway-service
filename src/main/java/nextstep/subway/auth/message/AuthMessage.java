package nextstep.subway.auth.message;

public enum AuthMessage {
    AUTH_ERROR_TOKEN_IS_NOT_VALID("Bearer Auth 토큰이 유효하지 않습니다.");

    private final String message;

    AuthMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
