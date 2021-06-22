package nextstep.subway.exception.line;

public class NotFoundSectionException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String NO_STATIONS = "구간이 없습니다.";

    public NotFoundSectionException() {
        super(NO_STATIONS);
    }

    public NotFoundSectionException(String message) {
        super(message);
    }
}
