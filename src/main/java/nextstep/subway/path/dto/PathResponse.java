package nextstep.subway.path.dto;

import java.util.*;

import nextstep.subway.line.domain.*;
import nextstep.subway.path.domain.*;
import nextstep.subway.station.domain.*;

public class PathResponse {
    private final List<Station> stations;
    private final int totalDistance;
    private final List<Line> lines;

    private PathResponse(List<Station> stations, int totalDistance, List<Line> lines) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.lines = lines;
    }

    public static PathResponse from(Path path) {
        return new PathResponse(path.getStations(), path.getTotalDistance(), path.getLines());
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

    @Override
    public String toString() {
        return "PathResponse{" +
            "stations=" + stations +
            ", totalDistance=" + totalDistance +
            ", lines=" + lines +
            '}';
    }
}
