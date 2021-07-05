package nextstep.subway.path.module;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.domain.SubwayPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JgraphtModule implements PathModule {

    private static final String NOT_CONNECTED_STATIONS_ERROR = "두 역이 연결되어 있지 않습니다.";

    public PathResponse findPath(List<Line> lines, Station sourceStation, Station targetStation) {
        SubwayGraph subwayGraph = new SubwayGraph(SectionEdge.class);
        subwayGraph.addAllVertex(lines);
        subwayGraph.addAllEdge(lines);

        GraphPath<Station, SectionEdge> path = new DijkstraShortestPath(subwayGraph).getPath(sourceStation, targetStation);
        validateConnect(path);
        SubwayPath subwayPath = new SubwayPath(path.getVertexList(), path.getEdgeList());

        return new PathResponse(StationResponse.ofList(subwayPath.stations()), subwayPath.distance());
    }

    private void validateConnect(GraphPath<Station, SectionEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException(NOT_CONNECTED_STATIONS_ERROR);
        }
    }
}
