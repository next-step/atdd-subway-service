package nextstep.subway.member.exception;

public class MemberException extends RuntimeException {
    public MemberException(final MemberExceptionType exceptionType) {
        super(exceptionType.getMessage());
    }

    public MemberException(final String message) {
        super(message);
    }
}
