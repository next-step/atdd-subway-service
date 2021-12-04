package nextstep.subway.line.exception;

public class CanNotDeleteSectionException extends IllegalArgumentException {
    private static final String DEFAULT_MESSAGE = "구간의 갯수가 한개일 경우 삭제할 수 없습니다.";

    public CanNotDeleteSectionException() {
        super(DEFAULT_MESSAGE);
    }
}
