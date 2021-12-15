package nextstep.subway.path.dto;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.exception.BadRequestApiException;

public class PathRequest {

    private final Long source;
    private final Long target;

    public PathRequest(Long source, Long target) {
        if (source.equals(target)) {
            throw new BadRequestApiException(ErrorCode.INVALID_PATH);
        }
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
