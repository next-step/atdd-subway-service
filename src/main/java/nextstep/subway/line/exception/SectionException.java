package nextstep.subway.line.exception;

import nextstep.subway.common.BusinessException;
import nextstep.subway.common.ErrorCode;

public class SectionException extends BusinessException {
	public SectionException(ErrorCode errorCode) {
		super(errorCode);
	}
}
