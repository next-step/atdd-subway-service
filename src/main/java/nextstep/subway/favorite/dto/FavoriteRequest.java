package nextstep.subway.favorite.dto;

public class FavoriteRequest {

    private Long sourceId;
    private Long targetId;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long source, Long target) {
        this.sourceId = source;
        this.targetId = target;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

}
