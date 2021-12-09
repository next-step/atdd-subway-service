package nextstep.subway.common.exception;

public class CyclePathException extends RuntimeException {
    private static final String CYCLE_PATH_EXCEPTION_MESSAGE = "출발역과 종착역이 같습니다.";
    private static final long serialVersionUID = 5L;

    public CyclePathException() {
        super(CYCLE_PATH_EXCEPTION_MESSAGE);
    }
}
