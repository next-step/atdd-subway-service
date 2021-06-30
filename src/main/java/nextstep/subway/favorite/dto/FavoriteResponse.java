package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

import java.util.List;

public class FavoriteResponse {
    private List<Favorite> favorites;

    public FavoriteResponse() {
        // empty
    }

    public FavoriteResponse(final List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }
}
