package nextstep.subway.path.domain;

import java.util.List;
import java.util.Set;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class Path {
    private final List<Station> stations;
    private final Distance distance;
    private final Set<Line> lines;

    private Path(List<Station> stations, Distance distance, Set<Line> lines) {
        this.stations = stations;
        this.distance = distance;
        this.lines = lines;
    }

    public static Path valueOf(List<Station> stations, Distance distance, Set<Line> lines) {
        return new Path(stations, distance, lines);
    }

    public List<Station> stations() {
        return stations;
    }

    public Distance distance() {
        return distance;
    }

    public Set<Line> lines() {
        return lines;
    }

    public int distanceValue() {
        return distance.distance();
    }
}
