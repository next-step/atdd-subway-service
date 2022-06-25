package nextstep.subway.member.exception;

public enum MemberExceptionType {
    NOT_EMAIL_REGEX("이메일 형식이 아닙니다."),
    INVALID_AGE("나이는 0살 이상 이여야 합니다."),
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다.");

    private final String message;

    MemberExceptionType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
