package nextstep.subway.line.application.exception;

public class LineNotFoundException extends RuntimeException {

    public static final LineNotFoundException NOT_FOUND_LINE = new LineNotFoundException("지하철 노선을 찾을 수 없습니다.");

    public LineNotFoundException(String message) {
        super(message);
    }
}
