package nextstep.subway.favorite.dto;

public class FavoriteRequest {
    private Integer source;
    private Integer target;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Integer source, Integer target) {
        this.source = source;
        this.target = target;
    }

    public Integer getSource() {
        return source;
    }

    public Integer getTarget() {
        return target;
    }
}
