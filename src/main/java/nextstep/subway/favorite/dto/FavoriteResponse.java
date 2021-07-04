package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.FavoriteSection;
import nextstep.subway.station.domain.Station;

public class FavoriteResponse {
    private Long id;
    private Station source;
    private Station target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(final FavoriteSection favoriteSection) {
        id = favoriteSection.getId();
        source = favoriteSection.getSource();
        target = favoriteSection.getTarget();
    }

    public static FavoriteResponse of(final FavoriteSection favoriteSection) {
        return new FavoriteResponse(favoriteSection);
    }

    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
