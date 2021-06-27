package nextstep.subway.line.exeption;

public class CanNotAddSectionException extends RuntimeException {

    public static final String MESSAGE = "등록할 수 없는 구간 입니다.";

    public CanNotAddSectionException() {
        super(MESSAGE);
    }
}
