package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

public class FavoriteRequest {
    private long source;
    private long target;

    protected FavoriteRequest() {}

    public FavoriteRequest(long source, long target) {
        this.source = source;
        this.target = target;
    }

    public long getSource() {
        return source;
    }

    public long getTarget() {
        return target;
    }

    public Favorite toFavorite(long memberId) {
        return new Favorite(memberId, source, target);
    }
}