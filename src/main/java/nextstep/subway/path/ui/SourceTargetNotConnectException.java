package nextstep.subway.path.ui;

public class SourceTargetNotConnectException extends RuntimeException {
    public SourceTargetNotConnectException() {
        super("출발역과 도착역이 연결이 되어있지 않습니다");
    }
}
