package nextstep.subway.favorite.dto;

public class FavoriteRequest {
    private String source;
    private String target;

    protected FavoriteRequest() {
    }

    public FavoriteRequest(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return Long.parseLong(source);
    }

    public Long getTarget() {
        return Long.parseLong(target);
    }
}
