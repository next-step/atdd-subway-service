package nextstep.subway.path.domain;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

import java.util.List;

import org.jgrapht.GraphPath;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class Path {

    private final GraphPath<Station, SectionEdge> value;

    Path(GraphPath<Station, SectionEdge> value) {
        this.value = value;
    }

    public static Path of(GraphPath<Station, SectionEdge> value) {
        return new Path(value);
    }

    public List<Station> getStations() {
        return unmodifiableList(value.getVertexList());
    }

    public List<Line> getPassingLines() {
        return value.getEdgeList().stream()
            .map(SectionEdge::getLine)
            .distinct()
            .collect(toList());
    }

    public double distance() {
        return value.getWeight();
    }
}
