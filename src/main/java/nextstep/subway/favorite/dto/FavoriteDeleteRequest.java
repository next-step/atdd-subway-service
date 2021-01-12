package nextstep.subway.favorite.dto;

public class FavoriteDeleteRequest {
    private long userId;
    private long favoriteId;

    public FavoriteDeleteRequest() {
    }

    public FavoriteDeleteRequest(long userId, long favoriteId) {
        this.userId = userId;
        this.favoriteId = favoriteId;
    }

    public long getUserId() {
        return userId;
    }

    public long getFavoriteId() {
        return favoriteId;
    }
}
