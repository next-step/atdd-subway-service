package nextstep.subway.common.Excetion;

public class StationNotFoundException extends RuntimeException {
    public static final String NOT_REGISTERED_STATION = "등록되지 않은 역입니다.";

    public StationNotFoundException() {
        super(NOT_REGISTERED_STATION);
    }
}
