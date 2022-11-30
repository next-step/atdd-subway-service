package nextstep.subway.line.exception;

public class IllegalDistanceException extends RuntimeException {

    public IllegalDistanceException() {
        super("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }
}
