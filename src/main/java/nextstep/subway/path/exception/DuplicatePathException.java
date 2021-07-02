package nextstep.subway.path.exception;

public class DuplicatePathException extends IllegalArgumentException {
    public static final String MESSAGE = "중복된 경로 입니다";

    public DuplicatePathException() {
        super(MESSAGE);
    }
}
