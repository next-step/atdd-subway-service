package nextstep.subway.favorite.dto;

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