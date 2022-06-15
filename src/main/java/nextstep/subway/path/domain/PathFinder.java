package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(final List<Line> lines) {
        for (Line line : lines) {
            addStationToGraphVertex(line.getStations());
            addSectionToEdgeAndSetWeight(line.getSections());
        }

        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public GraphPath<Station, DefaultWeightedEdge> getShortestPath(final Station sourceStation, final Station targetStation) {
        validInputCheck(sourceStation, targetStation);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        if (shortestPath == null) {
            throw new IllegalArgumentException("출발역과 도착역은 서로 연결이 되어있어야 합니다.");
        }
        return shortestPath;
    }

    private void addStationToGraphVertex(final List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addSectionToEdgeAndSetWeight(final List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    private void validInputCheck(final Station sourceStation, final Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역은 다른 역으로 지정되어야 합니다.");
        }
    }
}
