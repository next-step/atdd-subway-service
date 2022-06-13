package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public class PathFinder {

    private DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

    public PathFinder(List<Line> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("노선 정보가 없습니다.");
        }

        StationPathGraph stationPathGraph = new StationPathGraph(lines);
        shortestPath = new DijkstraShortestPath<>(stationPathGraph);
    }

    public Path findPath(Station sourceStation, Station targetStation) {
        validateStation(sourceStation, targetStation);

        GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(sourceStation, targetStation);
        if (path == null) {
            throw new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않습니다.");
        }

        return new Path(path.getVertexList(), (int) path.getWeight());
    }

    private void validateStation(Station sourceStation, Station targetStation) {
        if (Objects.equals(sourceStation, targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역은 같을 수 없습니다.");
        }

        if (sourceStation == null || targetStation == null) {
            throw new IllegalArgumentException("출발역과 도착역은 반드시 존재해야 합니다.");
        }
    }

}
