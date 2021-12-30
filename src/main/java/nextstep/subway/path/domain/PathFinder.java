package nextstep.subway.path.domain;

import com.google.common.collect.Lists;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathFinder {
    private final Lines lines;

    public PathFinder(Line line) {
        this(Lists.newArrayList(line));
    }

    public PathFinder(List<Line> lines) {
        this.lines = new Lines(lines);
    }

    public Path findPath(Station source, Station target) {
        PathGraph graph = new PathGraph();
        graph.makeGraph(lines);

        return graph.findShortestPath(source, target);
    }
}