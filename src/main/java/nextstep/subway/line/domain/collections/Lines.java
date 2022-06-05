package nextstep.subway.line.domain.collections;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public PathResponse findShortestPath(Station source, Station target) {
        validateEqualStation(source, target);

        GraphPath<Station, DefaultWeightedEdge> shortestPath = getShortestPath(source, target);
        List<Station> routes = shortestPath.getVertexList();
        int distance = (int) shortestPath.getWeight();

        return PathResponse.of(routes, distance);
    }

    private GraphPath<Station, DefaultWeightedEdge> getShortestPath(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(makeSubwayMap());
        GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(source, target);
        validatePath(shortestPath);
        return shortestPath;
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> makeSubwayMap() {
        WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines) {
            addVertexByStations(subwayMap, line.getStations());
            addEdgeBySections(subwayMap, line.getSections());
        }

        return subwayMap;
    }

    private void addEdgeBySections(WeightedMultigraph<Station, DefaultWeightedEdge> map, Sections sections) {
        for (Section section : sections.getSections()) {
            map.setEdgeWeight(map.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    private void addVertexByStations(WeightedMultigraph<Station, DefaultWeightedEdge> map, List<Station> stations) {
        for (Station station : stations) {
            map.addVertex(station);
        }
    }

    private void validatePath(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalStateException("[ERROR] 최단경로를 찾을 수 없습니다.");
        }
    }

    private void validateEqualStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("[ERROR] 출발역과 도착역은 동일할 수 없습니다.");
        }
    }
}
