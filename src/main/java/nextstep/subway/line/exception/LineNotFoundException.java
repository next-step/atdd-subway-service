package nextstep.subway.line.exception;

import nextstep.subway.common.error.ErrorCode;
import nextstep.subway.common.error.exception.EntityNotFoundException;

public class LineNotFoundException extends EntityNotFoundException {

    public LineNotFoundException() {
        super(ErrorCode.LINE_NOT_FOUND);
    }

    public LineNotFoundException(final String message) {
        super(message, ErrorCode.LINE_NOT_FOUND);
    }
}
