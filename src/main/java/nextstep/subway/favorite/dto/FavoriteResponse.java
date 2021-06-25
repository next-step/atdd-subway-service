package nextstep.subway.favorite.dto;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class FavoriteResponse {

    private final List<Station> stations;

    public FavoriteResponse(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }
}
