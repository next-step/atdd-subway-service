package nextstep.subway.exception;

public class NotEnrollStationInGraphException extends BadRequestException{
    private static final String EXCEPTION_MESSAGE = "구간에 찾는 지하철역이 없습니다.";

    public NotEnrollStationInGraphException() {
        super(EXCEPTION_MESSAGE);
    }
}
