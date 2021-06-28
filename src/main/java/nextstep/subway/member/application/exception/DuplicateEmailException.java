package nextstep.subway.member.application.exception;

public class DuplicateEmailException extends RuntimeException {
    private static final String DEFAULT_EXCEPTION_MESSAGE = "이메일이 중복됩니다.";

    public DuplicateEmailException() {
        super(DEFAULT_EXCEPTION_MESSAGE);
    }

    public DuplicateEmailException(String message) {
        super(message);
    }
}
