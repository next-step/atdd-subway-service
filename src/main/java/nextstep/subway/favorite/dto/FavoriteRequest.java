package nextstep.subway.favorite.dto;

public class FavoriteRequest {
    private long sourceStationId;
    private long targetStationId;

    public FavoriteRequest() {
    }

    public FavoriteRequest(long sourceStationId, long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public long getSourceStationId() {
        return sourceStationId;
    }

    public long getTargetStationId() {
        return targetStationId;
    }
}