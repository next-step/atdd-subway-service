package nextstep.subway.favorite.dto;

import javax.validation.constraints.NotNull;

public class FavoriteRequest {
    @NotNull(message = "출발역은 필수값입니다.")
    private Long source;

    @NotNull(message = "도착역은 필수값입니다.")
    private Long target;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long source, Long target) {
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
