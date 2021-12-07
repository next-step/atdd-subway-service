package nextstep.subway.path.infrastructure;


import java.util.List;
import nextstep.subway.common.exception.ErrorCode;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.path.dto.PathResultV2;
import nextstep.subway.station.domain.Station;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

public class SubwayPath {

    private final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath;

    public SubwayPath(Graph<Station, SectionEdge> graph) {
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public PathResultV2 getShortestPath(Station source, Station target) {
        GraphPath<Station, SectionEdge> graphPath = dijkstraShortestPath.getPath(source, target);
        List<Station> result = getVertexList(graphPath);
        int weight = (int) graphPath.getWeight();
        return PathResultV2.of(result, weight);
    }

    private List<Station> getVertexList(GraphPath<Station, SectionEdge> graphPath) {
        try {
            return graphPath.getVertexList();
        } catch (NullPointerException nullPointerException) {
            throw InvalidParameterException.of(ErrorCode.PATH_NOT_CONNECT);
        }
    }
}
