package nextstep.subway.line.application.exception;

public class LineNotFoundException extends RuntimeException {
    private static final String DEFAULT_EXCEPTION_MESSAGE = "요청한 정보에 해당하는 노선을 찾을 수 없습니다.";

    public LineNotFoundException(){
        super(DEFAULT_EXCEPTION_MESSAGE);
    }

    public LineNotFoundException(String message) {
        super(message);
    }
}
