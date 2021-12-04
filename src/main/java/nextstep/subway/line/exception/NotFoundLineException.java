package nextstep.subway.line.exception;

public class NotFoundLineException extends IllegalArgumentException {
    private static final String DEFAULT_MESSAGE = "노선의 정보를 찾을 수 없습니다.";

    public NotFoundLineException() {
        super(DEFAULT_MESSAGE);
    }
}
