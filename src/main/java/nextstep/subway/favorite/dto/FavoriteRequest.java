package nextstep.subway.favorite.dto;

public class FavoriteRequest {
    private String source;
    private String target;

    private FavoriteRequest() {
    }

    private FavoriteRequest(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public static FavoriteRequest of(Long source, Long target) {
        return new FavoriteRequest(source.toString(), target.toString());
    }

    public String getSource() {
        return this.source;
    }

    public String getTarget() {
        return this.target;
    }
}
