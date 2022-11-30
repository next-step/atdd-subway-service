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

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(),
                StationResponse.of(favorite.departureStation()),
                StationResponse.of(favorite.arrivalStation()));
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDepartureStation(StationResponse departureStation) {
        this.departureStation = departureStation;
    }

    public void setArrivalStation(StationResponse arrivalStation) {
        this.arrivalStation = arrivalStation;
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
