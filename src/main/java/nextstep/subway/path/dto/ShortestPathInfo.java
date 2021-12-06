package nextstep.subway.path.dto;

import java.util.Collections;
import java.util.List;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;

public class ShortestPathInfo {
    List<PathAnalysisKey> stations;
    List<Line> lines;
    Distance distance;

    private ShortestPathInfo(List<PathAnalysisKey> stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    private ShortestPathInfo(List<PathAnalysisKey> stations, Distance distance, List<Line> lines) {
        this.stations = stations;
        this.distance = distance;
        this.lines = lines;
    }

    public static ShortestPathInfo of(List<PathAnalysisKey> stations, Distance distance) {
        return new ShortestPathInfo(stations, distance);
    }

    public static ShortestPathInfo of(List<PathAnalysisKey> stations, Distance distance, List<Line> lines) {
        return new ShortestPathInfo(stations, distance, lines);
    }

    public List<PathAnalysisKey> getPathAnalysisKeys() {
        return Collections.unmodifiableList(this.stations);
    }

    public List<Line> getLines() {
        return Collections.unmodifiableList(this.lines);
    }

    public Distance getDistance() {
        return this.distance;
    }
}
