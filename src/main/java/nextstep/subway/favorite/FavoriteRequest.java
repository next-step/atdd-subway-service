package nextstep.subway.favorite;

public class FavoriteRequest {
    private final long sourceStationId;
    private final long targetStationId;

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
