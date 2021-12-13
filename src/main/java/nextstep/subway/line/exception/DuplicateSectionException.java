package nextstep.subway.line.exception;

import nextstep.subway.common.error.ErrorCode;
import nextstep.subway.common.error.exception.InvalidValueException;

public class DuplicateSectionException extends InvalidValueException {

    public DuplicateSectionException() {
        super(ErrorCode.SECTION_DUPLICATED);
    }
}
