package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponses {

    private List<FavoriteResponse> favoriteResponses;

    public FavoriteResponses() {
    }

    public FavoriteResponses(final List<Favorite> list) {
        this.favoriteResponses = list.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public List<FavoriteResponse> getFavoriteResponses() {
        return favoriteResponses;
    }

    public int size() {
        return favoriteResponses.size();
    }

}
