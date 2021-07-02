package nextstep.subway.exception;

public class NotMatchStationException extends RuntimeException {

    public static final String NOT_MATCH_STATION_EXCEPTION_MESSAGE = "등록할 수 없는 구간 입니다.";

    public NotMatchStationException() {
        super(NOT_MATCH_STATION_EXCEPTION_MESSAGE);
    }
}
