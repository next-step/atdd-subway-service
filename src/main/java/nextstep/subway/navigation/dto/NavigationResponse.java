package nextstep.subway.navigation.dto;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class NavigationResponse {
    private List<Station> stations;
    private int distance;
    private int fare;

    protected NavigationResponse() {
    }

    public NavigationResponse(List<Station> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static NavigationResponse of(List<Station> stations, int distance, int fare) {
        return new NavigationResponse(stations, distance, fare);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
