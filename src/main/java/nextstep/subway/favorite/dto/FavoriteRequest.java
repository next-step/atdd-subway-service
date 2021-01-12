package nextstep.subway.favorite.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FavoriteRequest {
    private Long source;
    private Long target;

    private FavoriteRequest() {
    }

    @Builder
    public FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }
}
