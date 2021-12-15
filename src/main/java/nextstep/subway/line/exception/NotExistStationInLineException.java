package nextstep.subway.line.exception;

public class NotExistStationInLineException extends RuntimeException {
    public NotExistStationInLineException(String msg) {
        super(msg);
    }
}
