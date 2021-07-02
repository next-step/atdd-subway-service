package nextstep.subway.exception;

public class DuplicateSectionException extends RuntimeException {

    public static final String DUPLICATE_SECTION_EXCEPTION_MESSAGE = "이미 등록된 구간 입니다.";

    public DuplicateSectionException() {
        super(DUPLICATE_SECTION_EXCEPTION_MESSAGE);
    }
}
