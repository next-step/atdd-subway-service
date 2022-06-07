package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

public class FavoriteResponse {

    private Long id;

    private FavoriteResponse() {}

    private FavoriteResponse(Long id) {
        this.id = id;
    }

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(favorite.getId());
    }

    public Long getId() {
        return this.id;
    }
}
