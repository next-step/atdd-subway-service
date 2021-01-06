package nextstep.subway.favorites.dto;

import nextstep.subway.favorites.domain.Favorites;
import nextstep.subway.station.dto.StationResponse;

public class FavoritesResponse {

    private long id;
    private StationResponse source;
    private StationResponse target;

    public FavoritesResponse() {
    }

    public FavoritesResponse(long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }

    public static FavoritesResponse of(Favorites favorites) {
        return new FavoritesResponse(favorites.getId(), StationResponse.of(favorites.getDeparture()), StationResponse.of(favorites.getArrival()));
    }
}
