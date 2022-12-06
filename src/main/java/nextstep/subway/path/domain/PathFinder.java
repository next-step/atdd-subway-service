package nextstep.subway.path.domain;

import nextstep.subway.ErrorMessage;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;

public class PathFinder {

    private Station sourceStation;
    private Station targetStation;
    private GraphPath sourceTargetGraphPath;

    public PathFinder(Station sourceStation, Station targetStation,
                      WeightedMultigraph<Station, DefaultWeightedEdge> sectionDistanceGraph) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(ErrorMessage.SOURCE_TARGET_EQUAL.getMessage());
        }
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.sourceTargetGraphPath = Optional.ofNullable(
                new DijkstraShortestPath(sectionDistanceGraph).getPath(sourceStation, targetStation))
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.DISCONNECT_SOURCE_TARGET.getMessage()));
    }

    public List<Station> getShortestPathStationList() {
        return sourceTargetGraphPath.getVertexList();
    }

    public int getShortestPathDistance() {
        return (int) sourceTargetGraphPath.getWeight();
    }

}
