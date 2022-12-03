package nextstep.subway.exception;

public class NotAllowRegisterSectionException extends RuntimeException {
    private final static String MESSAGE = "등록할 수 없는 구간 입니다.";

    public NotAllowRegisterSectionException() {
        super(MESSAGE);
    }
}
