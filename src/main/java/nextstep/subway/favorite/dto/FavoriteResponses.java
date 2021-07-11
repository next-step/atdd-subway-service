package nextstep.subway.favorite.dto;

import java.util.List;

public class FavoriteResponses {

    List<FavoriteResponse> favorites;

    public FavoriteResponses() {
    }

    public FavoriteResponses(List<FavoriteResponse> favorites) {
        this.favorites = favorites;
    }

    public List<FavoriteResponse> getFavorites() {
        return favorites;
    }
}
