package nextstep.subway.path.dto;

import java.util.List;

public class PathFinderResult {

    private List<Long> stationsIds;
    private int distance;

    public PathFinderResult(List<Long> stationsIds, int distance) {
        this.stationsIds = stationsIds;
        this.distance = distance;
    }

    public List<Long> getStationsIds() {
        return stationsIds;
    }

    public int getDistance() {
        return distance;
    }
}
