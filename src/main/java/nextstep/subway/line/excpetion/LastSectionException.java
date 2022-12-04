package nextstep.subway.line.excpetion;

public class LastSectionException extends BadRequestException {

    public static final String MESSAGE = "마지막 구간은 삭제할 수 없습니다.";

    public LastSectionException() {
        super(MESSAGE);
    }
}
