package nextstep.subway.domain.path.exception;

import nextstep.subway.global.error.ErrorCode;
import nextstep.subway.global.exception.BusinessException;

public class AmountException extends BusinessException {

    public AmountException(ErrorCode errorCode) {
        super(errorCode);
    }
}
