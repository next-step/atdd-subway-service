package nextstep.subway.favorite.dto;

public class FavoriteRequest {
    Long source;
    Long target;

    public FavoriteRequest(Long sourceStationId, Long targetStationId) {
        this.source = sourceStationId;
        this.target = targetStationId;
    }

    protected FavoriteRequest() {
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
