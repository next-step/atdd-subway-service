package nextstep.subway.line.exception;

public class BelowZeroDistanceException extends RuntimeException {
    public BelowZeroDistanceException() {
        super("거리는 0 이하가 될 수 없습니다.");
    }

    public BelowZeroDistanceException(String message) {
        super(message);
    }
}
