package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponses {

    List<FavoriteResponse> favorites;

    public FavoriteResponses() {
    }

    public FavoriteResponses(List<FavoriteResponse> favorites) {
        this.favorites = favorites;
    }

    public static FavoriteResponses of(List<Favorite> favorites) {
        List<FavoriteResponse> favoriteResponses = favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());

        return new FavoriteResponses(favoriteResponses);
    }

    public List<FavoriteResponse> getFavorites() {
        return favorites;
    }
}
