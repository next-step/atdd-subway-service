package nextstep.subway.exception;

public class SamePathException extends IllegalArgumentException {
    public SamePathException() {
        super("출발지하고 목적지가 같습니다.");
    }

    public SamePathException(String s) {
        super(s);
    }
}
