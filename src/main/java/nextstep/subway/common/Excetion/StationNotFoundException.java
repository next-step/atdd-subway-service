package nextstep.subway.common.Excetion;

public class StationNotFoundException extends RuntimeException {
    public StationNotFoundException() {
        super("등록되지 않은 역입니다.");
    }
}
