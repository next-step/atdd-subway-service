package nextstep.subway.common.exception;

public class NoRegisteredStationsException extends RuntimeException {
    private static final String NO_REGISTERED_STATIONS_MESSAGE = "등록할 수 없는 구간 입니다.";
    private static final long serialVersionUID = 4L;

    public NoRegisteredStationsException() {
        super(NO_REGISTERED_STATIONS_MESSAGE);
    }
}
