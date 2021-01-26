package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {

    private List<Station> shortestPath = new ArrayList<>();
    private Integer distance;
    private Integer totalFee;

    public PathResponse() {
    }

    public PathResponse(List<Station> shortestPath, int distance) {
        this.shortestPath = shortestPath;
        this.distance = distance;
    }

    public PathResponse(List<Station> shortestPath, int distance, int totalFee) {
        this.shortestPath = shortestPath;
        this.distance = distance;
        this.totalFee = totalFee;
    }

    public List<Station> getShortestPath() {
        return shortestPath;
    }

    public int getDistance() {
        return distance;
    }

    public Integer getTotalFee() {
        return totalFee;
    }
}
