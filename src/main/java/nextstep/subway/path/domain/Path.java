package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

public class Path {
    private final List<Station> stations;
    private final Integer distance;

    public Path(List<Station> stations, Integer distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static Path of(List<Station> vertexList, double weight) {
        return new Path(vertexList, (int) weight);
    }

    public List<Station> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }
}
