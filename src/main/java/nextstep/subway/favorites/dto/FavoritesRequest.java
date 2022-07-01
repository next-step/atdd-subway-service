package nextstep.subway.favorites.dto;

import nextstep.subway.favorites.domain.Favorites;

public class FavoritesRequest {

    private Long sourceId;
    private Long targetId;

    public FavoritesRequest() {
    }

    public FavoritesRequest(Long sourceId, Long targetId) {
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
