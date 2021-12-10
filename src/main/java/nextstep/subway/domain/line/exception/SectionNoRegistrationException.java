package nextstep.subway.domain.line.exception;

import nextstep.subway.global.error.ErrorCode;
import nextstep.subway.global.exception.BusinessException;

public class SectionNoRegistrationException extends BusinessException {

    public SectionNoRegistrationException(final String message) {
        super(message, ErrorCode.SECTION_NO_REGISTRATION);
    }

    public SectionNoRegistrationException() {
        super(ErrorCode.SECTION_NO_REGISTRATION);
    }
}
