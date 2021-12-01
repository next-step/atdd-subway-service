package nextstep.subway.exception;

public class NotFoundStationException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "지하철 역을 찾을 수 없습니다.";

    public NotFoundStationException() {
        super(EXCEPTION_MESSAGE);
    }
}
