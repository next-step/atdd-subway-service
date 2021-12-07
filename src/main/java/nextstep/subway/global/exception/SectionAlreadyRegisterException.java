package nextstep.subway.global.exception;

import nextstep.subway.global.error.ErrorCode;

public class SectionAlreadyRegisterException extends BusinessException {

    public SectionAlreadyRegisterException(final String message, final ErrorCode errorCode) {
        super(message, ErrorCode.SECTION_ALREADY_REGISTER);
    }

    public SectionAlreadyRegisterException() {
        super(ErrorCode.SECTION_ALREADY_REGISTER);
    }
}
