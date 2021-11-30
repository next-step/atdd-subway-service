package nextstep.exception;

public class StationNotFoundException extends BusinessException {

    private static final String ERROR_MESSAGE = "해당 역을 찾을 수 없습니다.";

    public StationNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
