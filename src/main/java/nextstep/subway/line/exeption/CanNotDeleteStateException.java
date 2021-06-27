package nextstep.subway.line.exeption;

public class CanNotDeleteStateException extends RuntimeException {

    public static final String MESSAGE = "삭제 할 수 없는 상태입니다.";

    public CanNotDeleteStateException() {
        super(MESSAGE);
    }
}
