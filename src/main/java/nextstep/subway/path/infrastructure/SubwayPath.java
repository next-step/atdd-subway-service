package nextstep.subway.path.infrastructure;


import java.util.List;
import java.util.Optional;
import nextstep.subway.common.exception.ErrorCode;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.path.dto.PathResult;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

public class SubwayPath {

    private final GraphPath<Station, SectionEdge> graphPath;

    public SubwayPath(SubwayGraph subwayGraph, Station source, Station target) {
        this.graphPath = createPath(subwayGraph, source, target);
    }

    public PathResult getShortestPath() {
        List<Station> result = graphPath.getVertexList();
        int weight = (int) graphPath.getWeight();

        return PathResult.of(result, weight);
    }

    private GraphPath<Station, SectionEdge> createPath(SubwayGraph subwayGraph, Station source,
        Station target) {
        isPathContains(subwayGraph, source, target);

        DijkstraShortestPath<Station, SectionEdge> path = new DijkstraShortestPath<>(subwayGraph);

        return Optional.ofNullable(path.getPath(source, target))
            .orElseThrow(() -> InvalidParameterException.of(ErrorCode.PATH_NOT_CONNECT));
    }

    private void isPathContains(SubwayGraph subwayGraph, Station source, Station target) {
        if (!subwayGraph.containsVertex(source) || !subwayGraph.containsVertex(target)) {
            throw InvalidParameterException.of(ErrorCode.PATH_IN_OUT_NOT_FOUND);
        }
    }
}
