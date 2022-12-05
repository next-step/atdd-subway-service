package nextstep.subway.favorite.domain;

public class FavoriteId {
    private final long favoriteId;

    private FavoriteId(long favoriteId) {
        this.favoriteId = favoriteId;
    }

    public static FavoriteId from(long favoriteId) {
        return new FavoriteId(favoriteId);
    }

    public static FavoriteId invalidId() {
        return new FavoriteId(-1);
    }

    public String getString() {
        return Long.toString(favoriteId);
    }
}
