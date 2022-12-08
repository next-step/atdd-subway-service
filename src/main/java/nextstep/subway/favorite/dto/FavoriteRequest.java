package nextstep.subway.favorite.dto;

public class FavoriteRequest {
    private final long source;
    private final long target;

    public FavoriteRequest(long source, long target) {
        this.source = source;
        this.target = target;
    }

    public long getSource() {
        return source;
    }

    public long getTarget() {
        return target;
    }
}
