package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class ShortestPath {
    private final List<Station> stations;
    private final Lines lines;
    private final long distance;

    public ShortestPath(List<Station> stations, Lines lines, long distance) {
        this.stations = stations;
        this.lines = lines;
        this.distance = distance;
    }

    public long getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Line mostExpensive() {
        return this.lines.mostExpensive();
    }
}
