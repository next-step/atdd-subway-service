package nextstep.subway.path.ui;

public class SameSourceTargetException extends RuntimeException {
    public SameSourceTargetException() {
        super("출발역과 도착역이 같습니다");
    }
}
