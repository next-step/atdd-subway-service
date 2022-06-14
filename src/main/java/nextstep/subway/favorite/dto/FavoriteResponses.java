package nextstep.subway.favorite.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.favorite.domain.Favorite;

public class FavoriteResponses {
    List<FavoriteResponse> favoriteResponses;

    public FavoriteResponses() {
    }

    public FavoriteResponses(List<Favorite> favorites) {
        this.favoriteResponses = favorites.stream().
                map(FavoriteResponse::new).
                collect(Collectors.toList());
    }

    public List<FavoriteResponse> getFavoriteResponses() {
        return favoriteResponses;
    }
}
