package nextstep.subway.exception;

public class NotFoundPathsException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "경로를 찾을 수 없습니다.";

    public NotFoundPathsException() {
        super(EXCEPTION_MESSAGE);
    }
}
