package nextstep.subway.exception;

public class CannotRemoveSectionException extends RuntimeException {
    private final static String MESSAGE = "구간을 제거할 수 없습니다.";

    public CannotRemoveSectionException() {
        super(MESSAGE);
    }
}
