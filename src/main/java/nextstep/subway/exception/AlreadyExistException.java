package nextstep.subway.exception;

public class AlreadyExistException extends IllegalArgumentException {

    public AlreadyExistException() {
        super("이미 등록된 구간 입니다.");
    }

    public AlreadyExistException(String s) {
        super(s);
    }
}
