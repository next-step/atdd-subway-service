package nextstep.subway.favorites.dto;

public class FavoritesRequest {

    private Long sourceStationId;
    private Long targetStationId;

    public FavoritesRequest(Long sourceStationId, Long targetStationId) {
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
