package nextstep.subway.path.dto;

import java.util.Objects;
import nextstep.subway.common.exception.ErrorCode;
import nextstep.subway.common.exception.InvalidParameterException;

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
            throw InvalidParameterException.of(ErrorCode.NOT_EMPTY);
        }
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

}
