package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponse {
    private List<FavoriteSection> favoriteSections;

    public FavoriteResponse(List<Favorite> favorites) {
        this.favoriteSections = favorites.stream()
                .map(FavoriteSection::from)
                .collect(Collectors.toList());
    }

    protected FavoriteResponse() {
    }

    public int size() {
        return this.favoriteSections.size();
    }

    public List<FavoriteSection> getFavoriteSections() {
        return this.favoriteSections;
    }

    public boolean empty() {
        return this.favoriteSections.isEmpty();
    }
}
