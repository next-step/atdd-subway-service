package nextstep.subway.line.exception;

public class InvalidRemoveSectionException extends IllegalArgumentException {

    public InvalidRemoveSectionException(String message) {
        super(message);
    }

    public InvalidRemoveSectionException() {
        this("구간을 삭제할 수 없습니다.");
    }
}
