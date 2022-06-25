package nextstep.subway.path.dto;

public class ShortestPathResponse {
    private StationsResponse stations;
    private double distance;

    public ShortestPathResponse() {
    }

    public ShortestPathResponse(StationsResponse stations, double distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public StationsResponse getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }
}
