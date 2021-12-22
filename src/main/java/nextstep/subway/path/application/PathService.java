package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.SubwayEdge;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.domain.SubwayPath;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    public SubwayPath findPath(List<Line> lines, Station sourceStation, Station targetStation) {
        sourceStation.validateNotSame(targetStation);

        SubwayGraph subwayGraph = new SubwayGraph(SubwayEdge.class);
        subwayGraph.addVertexWithStations(lines);
        subwayGraph.setEdgeWeightWithSections(lines);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(subwayGraph);
        return convertSubWayPath(dijkstraShortestPath.getPath(sourceStation, targetStation));
    }

    private SubwayPath convertSubWayPath(GraphPath path) {
        List<Section> sections = SubwayEdge.toSections(path.getEdgeList());
        return new SubwayPath(path.getVertexList(), sections, (int) path.getWeight());
    }
}
