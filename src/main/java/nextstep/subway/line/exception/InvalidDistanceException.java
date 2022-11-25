package nextstep.subway.line.exception;

public class InvalidDistanceException extends RuntimeException {

    public static final String INVALID_DISTANCE_MESSAGE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

    public InvalidDistanceException() {
        super(INVALID_DISTANCE_MESSAGE);
    }
}
