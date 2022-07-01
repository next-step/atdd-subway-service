package nextstep.subway.favorites.dto;

import nextstep.subway.favorites.domain.Favorites;
import nextstep.subway.station.dto.StationResponse;

public class FavoritesResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoritesResponse() {
    }

    private FavoritesResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoritesResponse from(Favorites favorites) {
        return new FavoritesResponse(
            favorites.getId(),
            StationResponse.from(favorites.getSource()),
            StationResponse.from(favorites.getTarget())
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
