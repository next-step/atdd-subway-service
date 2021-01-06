package nextstep.subway.path.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-02
 */
public class PathRequest {

    @NotNull
    @Positive
    private Long source;

    @NotNull
    @Positive
    private Long target;

    public PathRequest(Long source, Long target) {
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
