package nextstep.subway.path.domain;

import nextstep.subway.enums.ErrorMessage;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private static final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private static final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

    public PathFinder(List<Line> lines) {
        lines.forEach(line -> {
            addVertx(line);
            setEdgeWeight(line.getSections());
        });
    }

    private void addVertx(Line line) {
        line.getStations().forEach(graph::addVertex);
    }

    public Path findShortPath(Station startStation, Station endStation) {
        isValidDuplicatedStation(startStation, endStation);

        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(startStation, endStation);

        isValidExistStation(startStation, endStation);
        return new Path(path.getVertexList(), (int) path.getWeight());
    }

    private static void isValidExistStation(Station startStation, Station endStation) {
        if (!graph.containsVertex(startStation) || !graph.containsVertex(endStation)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXISTS_STATION_PATH.getMessage());
        }
    }

    private static void isValidDuplicatedStation(Station startStation, Station endStation) {
        if (startStation.equals(endStation)) {
            throw new IllegalArgumentException(ErrorMessage.DUPLICATED_STATION_PATH.getMessage());
        }
    }

    private void setEdgeWeight(List<Section> sections) {
        sections.forEach(section ->
                graph.setEdgeWeight(
                        graph.addEdge(section.getUpStation(), section.getDownStation()),
                        section.getDistance()
                ));
    }

}
