package nextstep.subway.line.exception;

public class InvalidRemoveSectionException extends IllegalArgumentException {

    public static final String ONE_SECTION_REMAINS = "구간이 1개인 노선은 구간을 삭제할 수 없습니다";

    public InvalidRemoveSectionException(String message) {
        super(message);
    }

    public InvalidRemoveSectionException() {
        this("구간을 삭제할 수 없습니다.");
    }
}
