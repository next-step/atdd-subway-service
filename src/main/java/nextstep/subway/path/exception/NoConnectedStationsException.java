package nextstep.subway.path.exception;

public class NoConnectedStationsException extends RuntimeException {
    public NoConnectedStationsException() {
        super("구간으로 연결되지 않은 역입니다.");
    }
}
