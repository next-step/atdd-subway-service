package nextstep.subway.favorite.dto;

import javax.validation.constraints.Positive;
import nextstep.subway.favorite.domain.Favorite;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-07
 */
public class FavoriteRequest {

    @Positive
    private Long source;

    @Positive
    private Long target;

    protected FavoriteRequest() {
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

    public Favorite toFavorite(long memberId) {
        return new Favorite(memberId, source, target);
    }
}
