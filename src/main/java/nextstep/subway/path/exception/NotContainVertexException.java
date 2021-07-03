package nextstep.subway.path.exception;

public class NotContainVertexException extends RuntimeException {
    public NotContainVertexException() {
        super("대상이 경로에 포함되어 있지 않습니다.");
    }

    public NotContainVertexException(String message) {
        super(message);
    }
}
