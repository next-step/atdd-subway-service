package nextstep.subway.favorite.dto;

public class FavoriteRequest {

    private Long source;
    private Long target;

    public FavoriteRequest(Long sourceStationId, Long targetStationId) {
        this.source = sourceStationId;
        this.target = targetStationId;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

}
