package nextstep.subway.path.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;

public class Path {
    private final List<Long> stations;
    private final int distance;
    private final List<SectionEdge> edges;

    public Path(List<Long> stations, int distance,
        List<SectionEdge> edges) {
        this.stations = stations;
        this.distance = distance;
        this.edges = edges;
    }

    public static Path of(List<Long> stations, int distance, List<SectionEdge> edges) {
        return new Path(stations, distance, edges);
    }

    public List<Long> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public Set<Line> getLines() {
        return edges.stream()
            .map(SectionEdge::getLine)
            .collect(Collectors.toSet());
    }
}
