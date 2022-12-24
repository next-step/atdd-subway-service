package nextstep.subway.path.domain;

import java.util.List;
import java.util.Set;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathResult {
    private List<Station> stations;
    private int distance;

    private Set<Line> lines;

    public PathResult(List<Station> stations, int distance, Set<Line> lines) {
        this.stations = stations;
        this.distance = distance;
        this.lines = lines;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public Set<Line> getLines() {
        return lines;
    }
}
