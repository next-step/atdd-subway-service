package nextstep.subway.member.dto;

public class FavoriteRequest {

    private Long sourceStationId;

    private Long targetStationId;

    protected FavoriteRequest() {
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
}
