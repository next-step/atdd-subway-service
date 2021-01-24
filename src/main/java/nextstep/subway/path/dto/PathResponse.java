package nextstep.subway.path.dto;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {

    private List<Long> shortestPath = new ArrayList<>();
    private Integer distance;
    private Integer totalFee;

    public PathResponse() {
    }

    public PathResponse(List<Long> shortestPath, int distance) {
        this.shortestPath = shortestPath;
        this.distance = distance;
    }

    public PathResponse(List<Long> shortestPath, int distance, int totalFee) {
        this.shortestPath = shortestPath;
        this.distance = distance;
        this.totalFee = totalFee;
    }

    public List<Long> getShortestPath() {
        return shortestPath;
    }

    public int getDistance() {
        return distance;
    }

    public Integer getTotalFee() {
        return totalFee;
    }
}
