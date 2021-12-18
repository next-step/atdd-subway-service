package nextstep.subway.favorite.dto;

import java.util.ArrayList;
import java.util.List;

public class FavoritesResponse {
    private List<FavoriteResponse> favoriteResponses = new ArrayList<>();

    protected FavoritesResponse() {
    }

    public FavoritesResponse(List<FavoriteResponse> favoriteResponses) {
        this.favoriteResponses = favoriteResponses;
    }

    public List<FavoriteResponse> getFavoriteResponses() {
        return favoriteResponses;
    }
}
