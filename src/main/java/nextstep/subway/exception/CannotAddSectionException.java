package nextstep.subway.exception;

public class CannotAddSectionException extends RuntimeException {
    private final static String MESSAGE = "구간을 추가할 수 없습니다.";

    public CannotAddSectionException() {
        super(MESSAGE);
    }
}
