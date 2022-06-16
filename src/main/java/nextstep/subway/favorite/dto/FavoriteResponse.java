package nextstep.subway.favorite.dto;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class FavoriteResponse {
    private Long id;
    private List<StationResponse> stations;

    public FavoriteResponse() {}

    public FavoriteResponse(Long id, List<StationResponse> stations) {
        this.id = id;
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
