package nextstep.subway.favorites.dto;

import nextstep.subway.favorites.domain.Favorite;
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

    public static FavoritesResponse of(Favorite favorite) {
        return new FavoritesResponse(favorite.getId(), StationResponse.of(favorite.getDeparture()), StationResponse.of(favorite.getArrival()));
    }
}
