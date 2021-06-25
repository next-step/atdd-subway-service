package nextstep.subway.common.Excetion;

public class LineNotFoundException extends RuntimeException {

    public static final String NOT_REGISTERED_LINE = "등록되지 않은 노선입니다.";

    public LineNotFoundException() {
        super(NOT_REGISTERED_LINE);
    }
}
