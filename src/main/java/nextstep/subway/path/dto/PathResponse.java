package nextstep.subway.path.dto;

import java.util.List;

public class PathResponse {

    private List<PathVertexStation> stations;

    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<PathVertexStation> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<PathVertexStation> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
