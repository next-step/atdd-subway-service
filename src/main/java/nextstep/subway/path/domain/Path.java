package nextstep.subway.path.domain;

import java.util.List;

public class Path {
    private final List<Long> stationIds;
    private final int distance;

    public Path(List<Long> stationIds, int distance) {
        this.stationIds = stationIds;
        this.distance = distance;
    }

    public static Path of(List<Long> stationIds, int distance) {
        return new Path(stationIds, distance);
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

    public int getDistance() {
        return distance;
    }
}