package nextstep.subway.path.dto;

import com.sun.istack.NotNull;
import lombok.Getter;

@Getter
public class PathRequest {
    @NotNull
    private Long source;
    @NotNull
    private Long target;

    public PathRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }
}
