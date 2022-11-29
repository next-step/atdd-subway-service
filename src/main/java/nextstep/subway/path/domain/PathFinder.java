package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        for (Line line : lines) {
            addStationVertex(line.stations());
            addEdgeSection(line.getSections());
        }
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void addStationVertex(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addEdgeSection(List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().value());
        }
    }

    public Path getShortestPath(Station sourceStation, Station targetStation) {
        validCheck(sourceStation, targetStation);
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        return new Path(graphPath.getVertexList(), (int) graphPath.getWeight());
    }

    private void validCheck(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 같은 경우 최단 경로를 조회할 수 없습니다.");
        }
        if (!graph.containsVertex(sourceStation) || !graph.containsVertex(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역은 서로 연결이 되어있어야 최단 경로를 조회할 수 있습니다.");
        }
    }

}
