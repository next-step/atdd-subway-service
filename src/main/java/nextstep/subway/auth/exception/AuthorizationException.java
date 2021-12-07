package nextstep.subway.auth.exception;

import nextstep.subway.common.BusinessException;
import nextstep.subway.common.ErrorCode;

public class AuthorizationException extends BusinessException {

	public AuthorizationException() {
		super(ErrorCode.UNAUTHORIZED_ERROR);
	}

	public AuthorizationException(ErrorCode errorCode) {
		super(errorCode);
	}
}
