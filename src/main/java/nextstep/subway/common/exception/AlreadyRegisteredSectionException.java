package nextstep.subway.common.exception;

public class AlreadyRegisteredSectionException extends RuntimeException {
    private static final String ALREADY_REGISTERED_SECTION_MESSAGE = "이미 등록된 구간 입니다.";
    private static final long serialVersionUID = 3L;

    public AlreadyRegisteredSectionException() {
        super(ALREADY_REGISTERED_SECTION_MESSAGE);
    }
}
