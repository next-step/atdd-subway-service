package nextstep.subway.favorite.dto;

public class FavoriteRequest {
    private String source;
    private String target;

    public FavoriteRequest(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return this.source;
    }

    public String getTarget() {
        return this.target;
    }
}
