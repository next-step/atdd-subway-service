package nextstep.subway.line.exception;

public class InvalidDistanceException extends RuntimeException {

    public static final String INVALID_DISTANCE_MESSAGE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";
    public static final String LESS_THAN_ZERO = "역간의 거리는 0보다 작거나 같을 수 없습니다.";

    public InvalidDistanceException() {
        super(INVALID_DISTANCE_MESSAGE);
    }

    public InvalidDistanceException(String message) {
        super(message);
    }
}
