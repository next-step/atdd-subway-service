package nextstep.subway.line.domain;

import nextstep.subway.global.BusinessException;

public class DistanceSplitFaildException extends BusinessException {

    public DistanceSplitFaildException(String message) {
        super(message);
    }
}
