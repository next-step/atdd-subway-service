package nextstep.subway.exception;

public class InvalidChargeException extends IllegalArgumentException {

    public InvalidChargeException() {
        super("유효하지 않은 요금입니다.");
    }

    public InvalidChargeException(String s) {
        super(s);
    }
}
