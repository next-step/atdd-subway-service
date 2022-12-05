package nextstep.subway.favorite.dto;

public class FavoriteCreatedRequest {
    private Long sourceId;
    private Long targetId;

    public FavoriteCreatedRequest(Long sourceId, Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Long getSourceId() {
        return this.sourceId;
    }

    public Long getTargetId() {
        return this.targetId;
    }
}
