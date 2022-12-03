package nextstep.subway.exception;

public class NotFoundException extends RuntimeException {
    private final static String MESSAGE = "리소스를 찾을 수 없습니다.";

    public NotFoundException() {
        super(MESSAGE);
    }
}
