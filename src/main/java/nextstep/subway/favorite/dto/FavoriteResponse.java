package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

public class FavoriteResponse {
    private Long id;

    public FavoriteResponse(Long id) {
        this.id = id;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId());
    }

    public Long getId() {
        return id;
    }
}
