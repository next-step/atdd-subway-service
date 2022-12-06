package nextstep.subway.line.exception;

public class InvalidDistanceException extends RuntimeException {

    public static final String LESS_THAN_ZERO = "역간의 거리는 0보다 작거나 같을 수 없습니다.";

    public InvalidDistanceException(String message) {
        super(message);
    }
}
