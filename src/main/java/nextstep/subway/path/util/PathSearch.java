package nextstep.subway.path.util;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Component;

@Component
public class PathSearch {

    public PathResponse findPaths(Lines lines, Station source, Station target) {
        final GraphGenerator graphGenerator = new GraphGenerator(lines);

        final DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graphGenerator.getGraph());
        final GraphPath<Station, SectionEdge> paths = dijkstraShortestPath.getPath(source, target);
        return new PathResponse(paths);
    }
}
