package nextstep.subway.exception;

public class DuplicatedSectionException extends RuntimeException {
    private final static String MESSAGE = "이미 등록된 구간 입니다.";

    public DuplicatedSectionException() {
        super(MESSAGE);
    }
}
