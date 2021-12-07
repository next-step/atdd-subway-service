package nextstep.subway.line.exception;

import nextstep.subway.common.BusinessException;
import nextstep.subway.common.ErrorCode;

public class LineException extends BusinessException {
	public LineException(ErrorCode errorCode) {
		super(errorCode);
	}
}
