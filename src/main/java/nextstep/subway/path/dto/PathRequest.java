package nextstep.subway.path.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

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
