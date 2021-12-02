package nextstep.subway.exception;

public class NotFoundSectionException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "구간을 찾을 수 없습니다.";

    public NotFoundSectionException() {
        super(EXCEPTION_MESSAGE);
    }
}
