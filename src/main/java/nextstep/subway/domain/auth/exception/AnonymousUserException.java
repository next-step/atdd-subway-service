package nextstep.subway.domain.auth.exception;

import nextstep.subway.global.error.ErrorCode;
import nextstep.subway.global.exception.BusinessException;

public class AnonymousUserException extends BusinessException {

    public AnonymousUserException() {
        super(ErrorCode.ANONYMOUS_USER);
    }
}
