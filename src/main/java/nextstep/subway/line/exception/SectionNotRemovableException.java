package nextstep.subway.line.exception;

import nextstep.subway.common.error.ErrorCode;
import nextstep.subway.common.error.exception.InvalidValueException;

public class SectionNotRemovableException extends InvalidValueException {

    public SectionNotRemovableException() {
        super(ErrorCode.SECTION_NOT_REMOVABLE);
    }

    public SectionNotRemovableException(final String message) {
        super(message, ErrorCode.SECTION_NOT_REMOVABLE);
    }
}
