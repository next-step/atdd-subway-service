package nextstep.subway.common.error.exception;

import nextstep.subway.common.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException() {
        super(ErrorCode.ENTITY_NOT_FOUND);
    }

    public EntityNotFoundException(final String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }

    public EntityNotFoundException(final ErrorCode errorCode) {
        super(errorCode);
    }

    public EntityNotFoundException(final String message, final ErrorCode errorCode) {
        super(message, errorCode);
    }
}
