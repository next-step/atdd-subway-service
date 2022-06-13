package nextstep.subway.favorite.dto;

public class FavoriteRequest {
    private Long sourceStationId;
    private Long targetStationId;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long sourceStationId, Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

}
