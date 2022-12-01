package nextstep.subway.favorite.dto;

public class FavoriteRequest {
    private Long sourceStationId;
    private Long targetStationId;

    private FavoriteRequest() {}

    public FavoriteRequest(final Long sourceStationId, final Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
