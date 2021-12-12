package nextstep.subway.domain.line.exception;

import nextstep.subway.global.error.ErrorCode;
import nextstep.subway.global.exception.BusinessException;

public class SectionDistanceFewerThanMaximum extends BusinessException {

    public SectionDistanceFewerThanMaximum(final String message) {
        super(message, ErrorCode.DISTANCE_FEWER_THAN_MAXIMUM);
    }

    public SectionDistanceFewerThanMaximum() {
        super(ErrorCode.DISTANCE_FEWER_THAN_MAXIMUM);
    }
}
