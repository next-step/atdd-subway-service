package nextstep.subway.station.application.exception;

public class StationNotFoundException extends RuntimeException {
    private static final String DEFAULT_EXCEPTION_MESSAGE = "요청한 정보에 해당하는 역을 찾을 수 없습니다.";

    public StationNotFoundException(){
        super(DEFAULT_EXCEPTION_MESSAGE);
    }

    public StationNotFoundException(String message) {
        super(message);
    }
}
