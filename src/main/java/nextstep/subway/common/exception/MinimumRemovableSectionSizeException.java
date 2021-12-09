package nextstep.subway.common.exception;

public class MinimumRemovableSectionSizeException extends RuntimeException {
    public static final String MINIMUM_REMOVABLE_SECTION_SIZE_EXCEPTION = "최소 삭제 가능한 구간 크기를 확인 해 주세요. : ";
    private static final long serialVersionUID = 1L;

    public MinimumRemovableSectionSizeException(int size) {
        super(MINIMUM_REMOVABLE_SECTION_SIZE_EXCEPTION + size);
    }
}
