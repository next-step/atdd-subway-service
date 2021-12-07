package nextstep.subway.global.exception;

import nextstep.subway.global.error.ErrorCode;

public class SectionDistanceLessThanMinimumException extends BusinessException {

    public SectionDistanceLessThanMinimumException(final String message) {
        super(message, ErrorCode.DISTANCE_LESS_THAN_MINIMUM);
    }

    public SectionDistanceLessThanMinimumException() {
        super(ErrorCode.DISTANCE_LESS_THAN_MINIMUM);
    }
}
