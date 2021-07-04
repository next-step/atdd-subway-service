package nextstep.subway.favorite.dto;

import java.util.ArrayList;
import java.util.List;

public class FavoriteResponses {
    private List<FavoriteResponse> favoriteResponses;

    public FavoriteResponses() {
    }

    public FavoriteResponses(List<FavoriteResponse> favoriteResponses) {
        this.favoriteResponses = new ArrayList<>(favoriteResponses);
    }

    public List<FavoriteResponse> getFavoriteResponses() {
        return favoriteResponses;
    }
}
