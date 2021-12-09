package nextstep.subway.common.exception;

public class NotFoundEntityException extends RuntimeException {
    public static final String NOT_FOUND_ENTITY_MESSAGE = "Entity를 찾지 못했습니다.";
    private static final long serialVersionUID = 2L;

    public NotFoundEntityException() {
        super(NOT_FOUND_ENTITY_MESSAGE);
    }
}
