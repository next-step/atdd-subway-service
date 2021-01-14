package nextstep.subway.favorite.dto;

public class FavoriteCreateRequest {
    private long userId;
    private long sourceStationId;
    private long targetStationId;

    public FavoriteCreateRequest() {
    }

    public FavoriteCreateRequest(long userid, long sourceStationId, long targetStationId) {
        this.userId = userid;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public long getUserId() {
        return userId;
    }

    public long getSourceStationId() {
        return sourceStationId;
    }

    public long getTargetStationId() {
        return targetStationId;
    }
}
