package nextstep.subway.common.Excetion;

public class LineNotFoundException extends RuntimeException {
    public LineNotFoundException() {
        super("등록되지 않은 노선입니다.");
    }
}
