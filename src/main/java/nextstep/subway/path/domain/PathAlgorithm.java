package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.graph.DefaultWeightedEdge;

public class PathAlgorithm {
    private final ShortestPathAlgorithm<Station, DefaultWeightedEdge> pathAlgorithm;

    private PathAlgorithm(ShortestPathAlgorithm<Station, DefaultWeightedEdge> pathAlgorithm) {
        this.pathAlgorithm = pathAlgorithm;
    }

    public GraphPath<Station, DefaultWeightedEdge> findShortestPath(Station sourceStation, Station targetStation) {
        try {
            return pathAlgorithm.getPath(sourceStation, targetStation);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("출발역과 도착역이 이어진 경로가 없습니다.");
        }
    }

    public static PathAlgorithm of(ShortestPathAlgorithm<Station, DefaultWeightedEdge> pathAlgorithm) {
        return new PathAlgorithm(pathAlgorithm);
    }
}
