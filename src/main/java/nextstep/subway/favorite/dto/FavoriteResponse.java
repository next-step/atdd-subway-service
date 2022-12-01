package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

public class FavoriteResponse {

    private Long id;

    private FavoriteResponse() {
    }

    public FavoriteResponse(Favorite favorite) {
        this.id = favorite.getId();
    }

    public Long getId() {
        return id;
    }
}
