package nextstep.subway.favorite.dto;

public class FavoriteRequest {
    private Long targetStationId;
    private Long sourceStationId;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long sourceStationId, Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }
}
