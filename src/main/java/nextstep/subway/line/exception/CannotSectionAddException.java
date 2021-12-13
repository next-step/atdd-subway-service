package nextstep.subway.line.exception;

import nextstep.subway.common.error.ErrorCode;
import nextstep.subway.common.error.exception.InvalidValueException;

public class CannotSectionAddException extends InvalidValueException {

    public CannotSectionAddException() {
        super(ErrorCode.SECTION_CANNOT_ADD);
    }
}
