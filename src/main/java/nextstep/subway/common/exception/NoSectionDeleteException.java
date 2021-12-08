package nextstep.subway.common.exception;

public class NoSectionDeleteException extends RuntimeException {

    private static final String NO_SECTION_DELETE = "등록된 구간이 없어서 삭제할 수 없습니다.";

    public NoSectionDeleteException() {
        super(NO_SECTION_DELETE);
    }
}
