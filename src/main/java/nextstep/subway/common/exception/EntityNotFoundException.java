package nextstep.subway.common.exception;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
