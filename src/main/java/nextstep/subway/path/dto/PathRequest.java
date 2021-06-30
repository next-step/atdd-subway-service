package nextstep.subway.path.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PathRequest {
    private long source;
    private long target;

    @Builder
    public PathRequest(final long source, final long target) {
        this.source = source;
        this.target = target;
    }

    public long getSource() {
        return source;
    }

    public void setSource(final long source) {
        this.source = source;
    }

    public long getTarget() {
        return target;
    }

    public void setTarget(final long target) {
        this.target = target;
    }
}
