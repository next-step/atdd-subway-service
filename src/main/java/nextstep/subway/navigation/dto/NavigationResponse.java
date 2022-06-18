package nextstep.subway.navigation.dto;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class NavigationResponse {
    private List<Station> stations;
    private int distance;

    protected NavigationResponse() {
    }

    public NavigationResponse(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static NavigationResponse of(List<Station> stations, int distance) {
        return new NavigationResponse(stations, distance);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
