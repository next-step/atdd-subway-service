package nextstep.subway.common.Excetion;

public class NotConnectStationException extends RuntimeException {
    public NotConnectStationException() {
        super("연결되어 있지 않은 경로 입니다.");
    }
}
