package nextstep.subway.exception;

public class NotFoundLineException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "노선을 찾을 수 없습니다.";

    public NotFoundLineException() {
        super(EXCEPTION_MESSAGE);
    }
}
