package nextstep.subway.path.exception;

public class NotConnectedPathException extends IllegalStateException{
    public static final String MESSAGE = "연결 되지 않는 경로 입니다";

    public NotConnectedPathException() {
        super(MESSAGE);
    }
}
