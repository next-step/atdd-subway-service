package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.Objects;

public class PathFinder {
    private static final String NONE_EQUAL_STATION = "출발역과 도착역 다른 경우만 조회할 수 있습니다";
    private static final String NONE_EXISTS_STATION = "출발역과 도착역이 모두 존재해야합니다";
    private static final String NONE_LINK_PATH = "출발역과 도착역이 연결되있어야 합니다";

    private static final String NULL_LINES = "노선이 존재해야합니다";

    private SubwayGraph subwayGraph;

    public PathFinder(List<Line> lines) {
        validateLines(lines);
        this.subwayGraph = new SubwayGraph(DefaultWeightedEdge.class, lines);

    }

    private void validateLines(List<Line> lines) {
        if (Objects.isNull(lines)) {
            throw new NullPointerException(NULL_LINES);
        }
    }

    public List<StationResponse> getShortestPath(Station sourceStation, Station targetStation) {
        vlidateStation(sourceStation, targetStation);
        return null;
    }

    private void vlidateStation(Station sourceStation, Station targetStation) {
        validateEqualStation(sourceStation, targetStation);
        validateExistsStation(sourceStation, targetStation);
        validateLinkStation(sourceStation, targetStation);
    }

    private void validateLinkStation(Station sourceStation, Station targetStation) {
        GraphPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(subwayGraph).getPath(sourceStation, targetStation);

        if (Objects.isNull(shortestPath)) {
            throw new IllegalArgumentException(NONE_LINK_PATH);
        }
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


}
