package nextstep.subway.favorite.dto;

public class FavoriteRequest {

    private Long source;
    private Long target;

    public FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
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
