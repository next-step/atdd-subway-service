package nextstep.subway.path.exception;

import nextstep.subway.common.ErrorCode;

public class PathException extends RuntimeException {
	public PathException(ErrorCode errorCode) {
		super(errorCode.getErrorMessage());
	}
}
