package nextstep.subway.path.dto;

import java.util.List;

public class PathResult {

    List<Long> stationIds;
    Integer distance;

    public PathResult(List<Long> stationIds, Integer distance) {
        this.stationIds = stationIds;
        this.distance = distance;
    }

    public static PathResult of(List<Long> stationIds, Integer distance) {
        return new PathResult(stationIds, distance);
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

    public Integer getDistance() {
        return distance;
    }
}
