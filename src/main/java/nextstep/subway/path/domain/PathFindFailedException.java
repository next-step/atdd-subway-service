package nextstep.subway.path.domain;

import nextstep.subway.common.domain.BusinessException;

public class PathFindFailedException extends BusinessException {

    public PathFindFailedException(String message) {
        super(message);
    }
}
