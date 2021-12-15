package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.domain.SubwayPath;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    public SubwayPath findPath(List<Line> lines, Station sourceStation, Station targetStation) {
        sourceStation.validateNotSame(targetStation);

        SubwayGraph subwayGraph = new SubwayGraph(DefaultWeightedEdge.class);
        subwayGraph.addVertexWithStations(lines);
        subwayGraph.setEdgeWeightWithSections(lines);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(subwayGraph);
        return convertSubWayPath(dijkstraShortestPath.getPath(sourceStation, targetStation));
    }

    private SubwayPath convertSubWayPath(GraphPath path) {
        return new SubwayPath(path.getVertexList(), (int) path.getWeight());
    }
}
