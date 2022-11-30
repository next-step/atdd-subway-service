package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse departureStation;
    private StationResponse arrivalStation;

    private FavoriteResponse(Long id, StationResponse departureStation, StationResponse arrivalStation) {
        this.id = id;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
    }

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(),
                StationResponse.of(favorite.departureStation()),
                StationResponse.of(favorite.arrivalStation()));
    }

    public Long getId() {
        return id;
    }

    public StationResponse getDepartureStation() {
        return departureStation;
    }

    public StationResponse getArrivalStation() {
        return arrivalStation;
    }
}
