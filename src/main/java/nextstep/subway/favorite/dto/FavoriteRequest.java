package nextstep.subway.favorite.dto;

public class FavoriteRequest {

    private String accessToken;
    private Long source;
    private Long target;

    public static FavoriteRequest of(String accessToken, Long source, Long target) {
        return new FavoriteRequest(accessToken, source, target);
    }

    public FavoriteRequest(String accessToken, Long source, Long target) {
        this.accessToken = accessToken;
        this.source = source;
        this.target = target;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
