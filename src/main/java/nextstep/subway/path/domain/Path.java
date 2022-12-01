package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final List<Station> stationPaths;
    private final Integer distance;

    private Path(List<Station> stationPaths, Integer distance) {
        this.stationPaths = stationPaths;
        this.distance = distance;
    }

    public static Path of(List<Station> stationPaths, Integer distance) {
        return new Path(stationPaths, distance);
    }

    public List<Station> getStationPaths() {
        return stationPaths;
    }

    public Integer getDistance() {
        return distance;
    }
}
