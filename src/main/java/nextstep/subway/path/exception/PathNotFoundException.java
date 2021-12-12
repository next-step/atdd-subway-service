package nextstep.subway.path.exception;

import nextstep.subway.common.error.ErrorCode;
import nextstep.subway.common.error.exception.EntityNotFoundException;

public class PathNotFoundException extends EntityNotFoundException {

    public PathNotFoundException() {
        super(ErrorCode.PATH_NOT_FOUND);
    }

    public PathNotFoundException(final String message) {
        super(message, ErrorCode.PATH_NOT_FOUND);
    }
}
