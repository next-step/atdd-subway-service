package nextstep.subway.favorite.dto;

public class FavoriteRequest {

    private final Long source;
    private final Long target;

    private FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public static FavoriteRequest of(Long source, Long target) {
        return new FavoriteRequest(source, target);
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
