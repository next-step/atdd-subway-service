package nextstep.subway.line.dto.path;

import java.util.Objects;
import nextstep.subway.common.exception.CommonErrorCode;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.line.application.exception.PathErrorCode;

public class PathRequest {

    private final Long source;
    private final Long target;

    public PathRequest(Long source, Long target) {
        validEmpty(source, target);
        this.source = source;
        this.target = target;
    }

    private void validEmpty(Long source, Long target) {
        if (Objects.isNull(source) || Objects.isNull(target)) {
            throw InvalidParameterException.of(PathErrorCode.NOT_EMPTY);
        }

        if (source.equals(target)) {
            throw InvalidParameterException.of(PathErrorCode.PATH_IN_OUT_SAME);
        }
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

}
