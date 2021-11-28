package nextstep.subway.favorite.dto;

public class FavoritePathRequest {
    private Long sourceId;
    private Long targetId;

    public FavoritePathRequest() {}

    private FavoritePathRequest(Long sourceId, Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public static FavoritePathRequest of(Long sourceId, Long targetId) {
        return new FavoritePathRequest(sourceId, targetId);
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }
}
