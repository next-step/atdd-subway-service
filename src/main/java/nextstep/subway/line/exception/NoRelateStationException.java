package nextstep.subway.line.exception;

public class NoRelateStationException extends RuntimeException {

    public NoRelateStationException() {
        super("등록할 수 없는 구간 입니다.");
    }
}
