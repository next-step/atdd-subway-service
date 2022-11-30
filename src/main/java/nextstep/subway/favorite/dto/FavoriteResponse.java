package nextstep.subway.favorite.dto;

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

    public static FavoriteResponse of(Long id, StationResponse departureStation, StationResponse arrivalStation) {
        return new FavoriteResponse(id, departureStation, arrivalStation);
    }
}
