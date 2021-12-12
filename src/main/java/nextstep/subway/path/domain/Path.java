package nextstep.subway.path.domain;

import java.util.*;

import nextstep.subway.line.domain.*;
import nextstep.subway.station.domain.*;

public class Path {
    private final List<Station> stations;
    private final int totalDistance;
    private final List<Line> lines;

    private Path(List<Station> stations, int totalDistance, List<Line> lines) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.lines = lines;
    }

    public static Path of(List<Station> stations, int totalDistance, List<Line> lines) {
        return new Path(stations, totalDistance, lines);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public List<Line> getLines() {
        return lines;
    }
}
