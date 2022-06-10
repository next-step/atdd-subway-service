package nextstep.subway.favorite.dto;

public class FavoriteCreateRequest {
    private String source;
    private String target;

    public FavoriteCreateRequest(String sourceStationId, String targetStationId) {
        this.source = sourceStationId;
        this.target = targetStationId;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
