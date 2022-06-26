package nextstep.subway.path.dto;

public class ShortestPathResponse {
    private StationsResponse stations;
    private int distance;

    public ShortestPathResponse() {
    }

    public ShortestPathResponse(StationsResponse stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public StationsResponse getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
