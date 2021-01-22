package nextstep.subway.path.dto;

import java.util.List;

public class PathResponse {

    private List<PathVertexStation> stations;

    private int distance;

    private int fare;

    public PathResponse() {
    }

    public PathResponse(List<PathVertexStation> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<PathVertexStation> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
