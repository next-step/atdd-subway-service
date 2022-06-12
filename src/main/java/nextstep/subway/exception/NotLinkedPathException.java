package nextstep.subway.exception;

public class NotLinkedPathException extends IllegalArgumentException {
    public NotLinkedPathException() {
        super("연결되어 있지 않은 경로입니다.");
    }

    public NotLinkedPathException(String s) {
        super(s);
    }
}
