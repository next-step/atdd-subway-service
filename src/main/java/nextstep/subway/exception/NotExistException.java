package nextstep.subway.exception;

public class NotExistException extends IllegalArgumentException {

    public NotExistException() {
        super("등록할 수 없는 구간 입니다.");
    }

    public NotExistException(String s) {
        super(s);
    }
}
