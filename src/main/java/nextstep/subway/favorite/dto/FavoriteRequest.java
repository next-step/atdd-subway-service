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

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    @Override
    public String toString() {
        return "FavoriteRequest{" +
                "sourceStationId=" + sourceStationId +
                ", targetStationId=" + targetStationId +
                '}';
    }
}
