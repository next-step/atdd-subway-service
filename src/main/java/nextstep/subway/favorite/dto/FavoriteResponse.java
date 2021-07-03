package nextstep.subway.favorite.dto;

import java.util.List;

import nextstep.subway.favorite.FavoriteSection;

public class FavoriteResponse {
    private final List<FavoriteSection> favoriteSections;

    public FavoriteResponse(final List<FavoriteSection> favoriteSections) {
        this.favoriteSections = favoriteSections;
    }

    public int size() {
        return favoriteSections.size();
    }

    public FavoriteSection get(final int index) {
        return favoriteSections.get(index);
    }
}
