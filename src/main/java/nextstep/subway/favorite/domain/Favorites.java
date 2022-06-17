package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.favorite.dto.FavoriteResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Favorites extends BaseEntity {
    private List<Favorite> favorites = new ArrayList<>();

    public Favorites(Collection<Favorite> favorites) {
        this.favorites.addAll(favorites);
    }

    public List<FavoriteResponse> toResponse() {
        return this.favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }
}
