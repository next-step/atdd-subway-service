package nextstep.subway.exception.line;

public class LineAlreadyExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String ALEADY_LINE = "이미 등록된 구간 입니다.";

    public LineAlreadyExistException() {
        super(ALEADY_LINE);
    }

    public LineAlreadyExistException(String message) {
        super(message);
    }
}
