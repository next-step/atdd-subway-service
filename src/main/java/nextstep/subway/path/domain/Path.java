package nextstep.subway.path.domain;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

import java.util.List;

import org.jgrapht.GraphPath;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class Path {

    private final List<Station> stations;
    private final List<Line> passingLines;
    private final double distance;

    Path(GraphPath<Station, SectionEdge> graphPath) {
        this.stations = graphPath.getVertexList();
        this.passingLines = graphPath.getEdgeList()
            .stream()
            .map(SectionEdge::getLine)
            .distinct()
            .collect(toList());
        this.distance = graphPath.getWeight();
    }

    public static Path of(GraphPath<Station, SectionEdge> value) {
        return new Path(value);
    }

    public List<Station> getStations() {
        return unmodifiableList(stations);
    }

    public List<Line> getPassingLines() {
        return unmodifiableList(passingLines);
    }

    public double distance() {
        return distance;
    }
}
