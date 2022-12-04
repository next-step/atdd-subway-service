package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

import static nextstep.subway.exception.ErrorMessage.NOT_CONNECT_START_ARRIVE_STATION;
import static nextstep.subway.exception.ErrorMessage.NOT_SEARCH_SAME_START_ARRIVE_STATION;

public class PathFinder {

    private final WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
    private final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath;

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
            SectionEdge sectionEdge = new SectionEdge(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
            graph.setEdgeWeight(sectionEdge, section.getDistance().value());
        }
    }

    public Path getShortestPath(Station sourceStation, Station targetStation) {
        validCheck(sourceStation, targetStation);
        GraphPath<Station, SectionEdge> graphPath = dijkstraShortestPath.getPath(sourceStation, targetStation);

        return new Path(graphPath.getVertexList(),
                graphPath.getEdgeList(),
                (int) graphPath.getWeight()
        );
    }

    private void validCheck(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(NOT_SEARCH_SAME_START_ARRIVE_STATION.getMessage());
        }
        if (!graph.containsVertex(sourceStation) || !graph.containsVertex(targetStation)) {
            throw new IllegalArgumentException(NOT_CONNECT_START_ARRIVE_STATION.getMessage());
        }
    }


}
