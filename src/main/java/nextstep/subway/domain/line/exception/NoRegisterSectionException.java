package nextstep.subway.domain.line.exception;

import nextstep.subway.global.error.ErrorCode;
import nextstep.subway.global.exception.BusinessException;

public class NoRegisterSectionException extends BusinessException {

    public NoRegisterSectionException(final String message) {
        super(message, ErrorCode.NO_REGISTER_SECTION);
    }

    public NoRegisterSectionException() {
        super(ErrorCode.NO_REGISTER_SECTION);
    }
}
