package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

public class PathFinder {

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private DijkstraShortestPath dijkstraShortestPath;

    public PathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
        this.dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    public static PathFinder of(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        for (Line line : lines) {
            addVertex(graph, line);
            settingEdgeWeight(graph, line);
        }
        return new PathFinder(graph);
    }

    public List<Station> findShortestPath(Station sourceStation, Station targetStation) {
        validateSameStation(sourceStation, targetStation);
        GraphPath graphPath = validateAndFindGraphPath(sourceStation, targetStation);
        return graphPath.getVertexList();
    }

    public int findShortestDistance(Station sourceStation, Station targetStation) {
        validateSameStation(sourceStation, targetStation);
        validateAndFindGraphPath(sourceStation, targetStation);
        return (int) dijkstraShortestPath.getPathWeight(sourceStation, targetStation);
    }

    private GraphPath validateAndFindGraphPath(Station sourceStation, Station targetStation) {
        GraphPath graphPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        if (Objects.isNull(graphPath)) {
            throw new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
        return graphPath;
    }

    private void validateSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation == targetStation) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    private static void addVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        List<Station> stations = line.getStations();
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private static void settingEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        List<Section> sections = line.getSections();
        for (Section section : sections) {
            graph.setEdgeWeight(
                    graph.addEdge(
                            section.getUpStation(),
                            section.getDownStation()),
                    section.getDistance());
        }
    }
}
