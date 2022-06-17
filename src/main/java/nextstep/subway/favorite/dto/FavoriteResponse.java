package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

public class FavoriteResponse {
    private Long id;
    private FavoriteStation source;
    private FavoriteStation target;

    private FavoriteResponse() {
    }

    private FavoriteResponse(Long id, FavoriteStation source, FavoriteStation target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                FavoriteStation.of(favorite.getSource()),
                FavoriteStation.of(favorite.getTarget()));
    }

    public Long getId() {
        return id;
    }

    public FavoriteStation getSource() {
        return source;
    }

    public FavoriteStation getTarget() {
        return target;
    }
}
