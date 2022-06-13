package nextstep.subway.favorite.dto;

public class FavoriteCreateRequest {
    private String sourceStationId;
    private String targetStationId;

    protected FavoriteCreateRequest() {

    }

    public FavoriteCreateRequest(String sourceStationId, String targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Long getSourceStationId() {
        return Long.parseLong(sourceStationId);
    }

    public Long getTargetStationId() {
        return Long.parseLong(targetStationId);
    }
}
