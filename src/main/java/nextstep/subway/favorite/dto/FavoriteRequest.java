package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

public class FavoriteRequest {
    private Long sourceId;
    private Long targetId;

    public static Favorite toFavorite(FavoriteRequest request) {
        return new Favorite(request.getSourceId(), request.getTargetId());
    }

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long sourceId, Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }
}
