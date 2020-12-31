package nextstep.subway.path.domain;

import nextstep.subway.path.domain.exceptions.PathFindingException;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
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

    public List<Long> findShortestPath(Long sourceId, Long destinationId) {
        try {
            DijkstraShortestPath shortestPath = new DijkstraShortestPath(path);

            return shortestPath.getPath(sourceId, destinationId).getVertexList();
        } catch (NullPointerException e) {
            throw new PathFindingException("경로가 존재하지 않습니다.");
        } catch (IllegalArgumentException e) {
            throw new PathFindingException("경로에 없는 역을 탐색 대상으로 지정할 수 없습니다.");
        }
    }
}
