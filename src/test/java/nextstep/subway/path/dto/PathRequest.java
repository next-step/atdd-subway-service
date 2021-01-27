package nextstep.subway.path.dto;

import lombok.Getter;

@Getter
public class PathRequest {
    private Long source;
    private Long target;

    public PathRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }
}
