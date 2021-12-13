package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class ShortestPathResponse {

    private List<Station> stations;
    private int distance;
    private int price;

    public ShortestPathResponse() {
    }

    public ShortestPathResponse(List<Station> stations, int distance, int price) {
        this.stations = stations;
        this.distance = distance;
        this.price = price;
    }

    public static ShortestPathResponse of(List<Station> stations, int shortestDistance, int price) {
        return new ShortestPathResponse(stations, shortestDistance, price);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getPrice() {
        return price;
    }
}
