package nextstep.subway.line.infrastructure.path;

import java.util.List;
import java.util.Optional;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.line.application.exception.PathErrorCode;
import nextstep.subway.line.dto.path.PathResult;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

public class SubwayPath {

    private final DijkstraShortestPath<Station, SectionEdge> path;

    public SubwayPath(DijkstraShortestPath<Station, SectionEdge> path) {
        this.path = path;
    }

    public static SubwayPath of(SubwayGraph subwayGraph) {
        return new SubwayPath(new DijkstraShortestPath<>(subwayGraph));
    }

    public PathResult getShortestPath(Station source, Station target) {
        GraphPath<Station, SectionEdge> graphPath = createPath(source, target);

        List<Station> result = graphPath.getVertexList();
        int weight = (int) graphPath.getWeight();

        return PathResult.of(result, weight);
    }

    private GraphPath<Station, SectionEdge> createPath(Station source, Station target) {
        return Optional.ofNullable(path.getPath(source, target))
            .orElseThrow(() -> InvalidParameterException.of(PathErrorCode.PATH_NOT_CONNECT));
    }
}
