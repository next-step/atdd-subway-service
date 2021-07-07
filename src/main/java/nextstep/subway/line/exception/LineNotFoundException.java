package nextstep.subway.line.exception;

public class LineNotFoundException extends RuntimeException{
    public LineNotFoundException() {
        super("노선을 찾을 수 없습니다.");
    }
}
