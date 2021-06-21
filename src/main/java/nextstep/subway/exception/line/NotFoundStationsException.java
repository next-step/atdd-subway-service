package nextstep.subway.exception.line;

public class NotFoundStationsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String NO_STATIONS = "등록할 수 없는 구간 입니다.";

    public NotFoundStationsException() {
        super(NO_STATIONS);
    }

    public NotFoundStationsException(String message) {
        super(message);
    }
}
