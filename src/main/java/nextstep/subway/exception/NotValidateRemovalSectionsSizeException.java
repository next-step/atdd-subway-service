package nextstep.subway.exception;

public class NotValidateRemovalSectionsSizeException extends RuntimeException {

    public static final String NOT_VALIDATE_REMOVAL_SIZE_EXCEPTION_SIZE = "구간을 제거할 수 있는 최소 크기보다 작습니다.";

    public NotValidateRemovalSectionsSizeException() {
        super(NOT_VALIDATE_REMOVAL_SIZE_EXCEPTION_SIZE);
    }
}
