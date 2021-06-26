package nextstep.subway.path.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PathRequest {
    private int source;
    private int target;

    @Builder
    public PathRequest(final int source, final int target) {
        this.source = source;
        this.target = target;
    }
}
