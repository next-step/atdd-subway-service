package nextstep.subway.line.domain;

import nextstep.subway.global.domain.BusinessException;

public class DistanceSplitFaildException extends BusinessException {

    public DistanceSplitFaildException(String message) {
        super(message);
    }
}
