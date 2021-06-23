package nextstep.subway.path.exception;

public class SameOriginAndDestinationException extends RuntimeException {

    public SameOriginAndDestinationException() {
        super("출발역과 도착역이 같습니다.");
    }

}
