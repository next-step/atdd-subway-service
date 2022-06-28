package nextstep.subway.path.domain;

import java.util.List;
import java.util.Set;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class Path {
    private List<Station> stations;
    private Set<Line> lines;
    private Integer distance;

    public Path() {
    }

    public Path(final List<Station> stations, final Set<Line> lines, final Integer distance) {
        this.stations = stations;
        this.lines = lines;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Set<Line> getLines() {
        return lines;
    }

    public Integer getDistance() {
        return distance;
    }
}
