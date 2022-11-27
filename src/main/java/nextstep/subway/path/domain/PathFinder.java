package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.ui.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final List<Line> lines;

    private PathFinder(List<Line> lines) {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.lines = lines;
    }

    public static PathFinder from(final List<Line> lines) {
        return new PathFinder(lines);
    }

    public PathResponse getShortestPath(final Long sourceId, final Long targetId) {
        return null;
    }
}
