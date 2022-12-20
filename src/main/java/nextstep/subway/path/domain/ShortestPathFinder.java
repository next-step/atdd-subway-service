package nextstep.subway.path.domain;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class ShortestPathFinder implements PathFinder{
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);

    public static ShortestPathFinder from(List<Section> sections) {
        return new ShortestPathFinder(sections);
    }

    private ShortestPathFinder(List<Section> sections) {
        sections.forEach(eachSection -> {
            addVertex(eachSection.getUpStation());
            addVertex(eachSection.getDownStation());
            graph.setEdgeWeight(graph.addEdge(eachSection.getUpStation(), eachSection.getDownStation()), eachSection.getDistance());
        });
    }

    @Override
    public List<Station> findAllStationsInTheShortestPath(Station sourceStation, Station targetStation) {
        GraphPath<Station, DefaultWeightedEdge> path = getShortestPath(sourceStation, targetStation);
        return path.getVertexList();
    }

    @Override
    public int findTheShortestPathDistance(Station sourceStation, Station targetStation) {
        GraphPath<Station, DefaultWeightedEdge> path = getShortestPath(sourceStation, targetStation);
        return (int) path.getWeight();
    }

    private void addVertex(Station station) {
        if (graph.containsVertex(station)) {
            return;
        }
        graph.addVertex(station);
    }

    private GraphPath<Station, DefaultWeightedEdge> getShortestPath(Station sourceStation, Station targetStation) {
        checkSourceAndTargetIsEqual(sourceStation, targetStation);
        GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(sourceStation, targetStation);
        checkPathExist(path);
        return path;
    }

    private void checkSourceAndTargetIsEqual(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(ErrorCode.SOURCE_AND_TARGET_EQUAL.getErrorMessage());
        }
    }

    private void checkPathExist(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException(ErrorCode.SOURCE_NOT_CONNECTED_TO_TARGET.getErrorMessage());
        }
    }
}
