package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public class Path {

    private final List<Line> lines;
    private final List<Station> stations;
    private final Distance distance;

    private Path(final List<Line> lines, final List<Station> stations, final Distance distance) {
        this.lines = lines;
        this.stations = stations;
        this.distance = distance;
    }

    public static Path of(final GraphPath<Station, SectionEdge> graphPath) {
        return new Path(getPathLines(graphPath), graphPath.getVertexList(),
            Distance.of(graphPath.getWeight()));
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public Distance getDistance() {
        return distance;
    }

    public int additionalFare() {
        return lines.stream()
            .map(Line::getAdditionalFare)
            .max(Integer::compareTo)
            .orElse(Line.DEFAULT_ADDITIONAL_FARE);
    }

    private static List<Line> getPathLines(final GraphPath<Station, SectionEdge> graphPath) {
        return graphPath.getEdgeList().stream()
            .map(SectionEdge::getLine)
            .distinct()
            .collect(Collectors.toList());
    }
}
