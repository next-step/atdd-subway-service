package nextstep.subway.exception;

public class NotIncludeStationException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "두 지하철역 중 하나는 등록 되어 있어야 합니다.";

    public NotIncludeStationException() {
        super(EXCEPTION_MESSAGE);
    }
}
