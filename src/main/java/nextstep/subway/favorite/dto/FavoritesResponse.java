package nextstep.subway.favorite.dto;

import java.util.List;

public class FavoritesResponse {
    private List<FavoriteResponse> favoriteResponses;

    public FavoritesResponse() {
    }

    public FavoritesResponse(List<FavoriteResponse> favoriteResponses) {
        this.favoriteResponses = favoriteResponses;
    }

    public static FavoritesResponse of(List<FavoriteResponse> favoriteResponses) {
        return new FavoritesResponse(favoriteResponses);
    }

    public List<FavoriteResponse> getFavoriteResponses() {
        return favoriteResponses;
    }
}
