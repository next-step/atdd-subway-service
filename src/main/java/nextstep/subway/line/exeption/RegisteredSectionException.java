package nextstep.subway.line.exeption;

public class RegisteredSectionException extends RuntimeException {

    public static final String MESSAGE = "이미 등록 된 구간입니다.";

    public RegisteredSectionException() {
        super(MESSAGE);
    }
}
