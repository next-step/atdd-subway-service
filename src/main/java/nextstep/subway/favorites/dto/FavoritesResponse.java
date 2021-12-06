package nextstep.subway.favorites.dto;

import nextstep.subway.favorites.domain.Favorites;
import nextstep.subway.station.dto.StationResponse;

public class FavoritesResponse {
    private final Long id;
    private final StationResponse source;
    private final StationResponse target;

    public FavoritesResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoritesResponse of(Favorites favorites) {
        return new FavoritesResponse(favorites.getId(),
                StationResponse.of(favorites.getUpStation()),
                StationResponse.of(favorites.getDownStation())
        );
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
