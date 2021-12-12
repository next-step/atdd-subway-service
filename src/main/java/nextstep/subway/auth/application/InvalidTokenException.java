package nextstep.subway.auth.application;

import nextstep.subway.common.error.ErrorCode;
import nextstep.subway.common.error.exception.BusinessException;

public class InvalidTokenException extends BusinessException {

    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
