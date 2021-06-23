package nextstep.subway.path.exception;

public class CannotFoundPathException extends RuntimeException {

    public CannotFoundPathException() {
        super("경로를 찾을 수 없습니다.");
    }

}
