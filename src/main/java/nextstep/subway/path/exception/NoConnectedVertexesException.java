package nextstep.subway.path.exception;

public class NoConnectedVertexesException extends RuntimeException {
    public NoConnectedVertexesException() {
        super("대상들이 연결되어 있지 않습니다.");
    }
}
