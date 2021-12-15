package nextstep.subway.favorite.dto;

public class FavoriteRequest {

    private final Long sourceId;
    private final Long targetId;

    public FavoriteRequest(final Long sourceId, final Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }
}
