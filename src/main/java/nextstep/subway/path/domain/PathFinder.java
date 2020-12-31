package nextstep.subway.path.domain;

import nextstep.subway.path.domain.exceptions.PathFindingException;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private final WeightedMultigraph<Long, DefaultWeightedEdge> path;

    PathFinder(WeightedMultigraph<Long, DefaultWeightedEdge> path) {
        this.path = path;
    }

    public static PathFinder of(List<Long> stationIds, List<SafeSectionInfo> safeSectionInfos) {
        WeightedMultigraph path = PathFactory.of(stationIds, safeSectionInfos);

        return new PathFinder(path);
    }

    public ShortestPath findShortestPath(Long sourceId, Long destinationId) {
        validate(sourceId, destinationId);
        return ShortestPath.of(this.path, sourceId, destinationId);
    }

    private void validate(Long sourceId, Long destinationId) {
        if (sourceId.equals(destinationId)) {
            throw new PathFindingException("출발지와 도착지는 달라야 합니다.");
        }
    }
}
