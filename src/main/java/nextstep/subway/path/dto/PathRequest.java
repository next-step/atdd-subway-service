package nextstep.subway.path.dto;

import nextstep.subway.common.exception.SubwayErrorCode;
import nextstep.subway.common.exception.SubwayException;

public class PathRequest {
    private long source;
    private long target;

    public PathRequest(long source, long target) {
        validate(source, target);
        this.source = source;
        this.target = target;
    }

    private void validate(long source, long target) {
        if (source == target) {
            throw new SubwayException(SubwayErrorCode.SAME_SOURCE_AND_TARGET);
        }
    }

    public long getSource() {
        return source;
    }

    public long getTarget() {
        return target;
    }
}
