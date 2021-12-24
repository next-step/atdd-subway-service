package nextstep.subway.common.exception;

public class InvalidDataException extends RuntimeException {

    public InvalidDataException() {
        super("잘못된 값입니다.");
    }

    public InvalidDataException(String message) {
        super(message);
    }

}
