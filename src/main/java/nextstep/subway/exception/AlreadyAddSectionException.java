package nextstep.subway.exception;

public class AlreadyAddSectionException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "이미 등록된 구간 입니다.";

    public AlreadyAddSectionException() {
        super(EXCEPTION_MESSAGE);
    }
}
