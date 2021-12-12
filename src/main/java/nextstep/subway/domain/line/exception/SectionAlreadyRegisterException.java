package nextstep.subway.domain.line.exception;

import nextstep.subway.global.error.ErrorCode;
import nextstep.subway.global.exception.BusinessException;

public class SectionAlreadyRegisterException extends BusinessException {

    public SectionAlreadyRegisterException(final String message, final ErrorCode errorCode) {
        super(message, ErrorCode.SECTION_ALREADY_REGISTER);
    }

    public SectionAlreadyRegisterException() {
        super(ErrorCode.SECTION_ALREADY_REGISTER);
    }
}
