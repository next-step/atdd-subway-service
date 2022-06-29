package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;

public class FavoriteRequest {
    private long sourceId;
    private long targetId;

    public FavoriteRequest() {
    }

    public FavoriteRequest(long sourceId, long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public long getSourceId() {
        return sourceId;
    }

    public long getTargetId() {
        return targetId;
    }

}
