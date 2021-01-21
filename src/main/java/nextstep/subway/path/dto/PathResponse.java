package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {

    private List<Station> shortestPath = new ArrayList<>();
    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<Station> shortestPath, int distance) {
        this.shortestPath = shortestPath;
        this.distance = distance;
    }

    public List<Station> getShortestPath() {
        return shortestPath;
    }

    public int getDistance() {
        return distance;
    }
}
