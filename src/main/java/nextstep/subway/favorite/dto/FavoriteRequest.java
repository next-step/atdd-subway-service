package nextstep.subway.favorite.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FavoriteRequest {
    private Long source;
    private Long target;

    public FavoriteRequest(final Long source, final Long target) {
        this.source = source;
        this.target = target;
    }
}
