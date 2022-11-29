package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Distance;

public class Path {
    private final List<Long> stationIds;
    private final Distance distance;

    public Path(List<Long> stationIds, int distance) {
        this.stationIds = stationIds;
        this.distance = Distance.from(distance);
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

    public int getDistance() {
        return distance.getDistance();
    }
}