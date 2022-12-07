package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathFinder {
    private static final String NONE_EQUAL_STATION = "출발역과 도착역 다른 경우만 조회할 수 있습니다";
    private static final String NONE_EXISTS_STATION = "출발역과 도착역이 노선에 존재해야합니다";
    private static final String NONE_LINK_PATH = "출발역과 도착역이 연결되있어야 합니다";
    private static final String NULL_LINES = "노선이 존재해야합니다";
    private SubwayGraph subwayGraph;

    public PathFinder(List<Line> lines) {
        validateLines(lines);
        this.subwayGraph = new SubwayGraph(lines);
    }

    private void validateLines(List<Line> lines) {
        if (Objects.isNull(lines)) {
            throw new NullPointerException(NULL_LINES);
        }
    }

/*    public List<Station> getShortestPath(Station sourceStation, Station targetStation) {
        GraphPath<Station, DefaultWeightedEdge> shortestPath = validateLinkGraph(sourceStation, targetStation);
        return shortestPath
                .getVertexList()
                .stream()
                .collect(Collectors.toList());
    }*/

    public ShortestPath getShortestPath(Station sourceStation, Station targetStation) {
        return new ShortestPath(validateLinkGraph(sourceStation, targetStation));
    }

    private void vlidateStation(Station sourceStation, Station targetStation) {
        validateEqualStation(sourceStation, targetStation);
        validateExistsStation(sourceStation, targetStation);
    }

    private GraphPath<Station, DefaultWeightedEdge> validateLinkGraph(Station sourceStation, Station targetStation) {
        vlidateStation(sourceStation, targetStation);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(subwayGraph).getPath(sourceStation, targetStation);
        if (Objects.isNull(shortestPath)) {
            throw new IllegalArgumentException(NONE_LINK_PATH);
        }
        return shortestPath;
    }

    private void validateEqualStation(Station sourceStation, Station targetStation) {
        if (Objects.equals(sourceStation, targetStation)) {
            throw new IllegalArgumentException(NONE_EQUAL_STATION);
        }
    }

    private void validateExistsStation(Station sourceStation, Station targetStation) {
        if (!subwayGraph.containsVertex(sourceStation) || !subwayGraph.containsVertex(targetStation)) {
            throw new IllegalArgumentException(NONE_EXISTS_STATION);
        }
    }

    public int getShortestDistance(Station sourceStation, Station targetStation) {
        GraphPath<Station, DefaultWeightedEdge> shortestPath = validateLinkGraph(sourceStation, targetStation);
        return (int) shortestPath.getWeight();
    }
}
