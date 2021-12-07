package nextstep.subway.global.exception;

import nextstep.subway.global.error.ErrorCode;

public class SectionNoRegistrationException extends BusinessException {

    public SectionNoRegistrationException(final String message) {
        super(message, ErrorCode.SECTION_NO_REGISTRATION);
    }

    public SectionNoRegistrationException() {
        super(ErrorCode.SECTION_NO_REGISTRATION);
    }
}
