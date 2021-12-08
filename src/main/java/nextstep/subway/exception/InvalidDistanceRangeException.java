package nextstep.subway.exception;

public class InvalidDistanceRangeException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "거리 숫자의 범위가 잘못되었습니다.";

    public InvalidDistanceRangeException() {
        super(EXCEPTION_MESSAGE);
    }
}
