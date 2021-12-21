package nextstep.subway.path.exception;

public class SameStartEndStationException extends RuntimeException {
    public SameStartEndStationException() {
        super("출발역과 도착역이 달라야합니다.");
    }
}
