package nextstep.subway.global.exception;

import nextstep.subway.global.error.ErrorCode;

public class NoRegisterSectionException extends BusinessException {

    public NoRegisterSectionException(final String message) {
        super(message, ErrorCode.NO_REGISTER_SECTION);
    }

    public NoRegisterSectionException() {
        super(ErrorCode.NO_REGISTER_SECTION);
    }
}
