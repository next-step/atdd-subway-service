package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        for (Line line : lines) {
            addStationToGraphVertex(line.getStations(), graph);
            addEdgeAndSetEdgeWeight(line.getSections(), graph);
        }
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void addStationToGraphVertex(List<Station> stations, WeightedMultigraph<Station, SectionEdge> graph) {
        stations.forEach(graph::addVertex);
    }

    private void addEdgeAndSetEdgeWeight(List<Section> sections, WeightedMultigraph<Station, SectionEdge> graph) {
        for (Section section : sections) {
            SectionEdge sectionEdge = new SectionEdge(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
            graph.setEdgeWeight(sectionEdge, section.getDistanceValue());
        }
    }

    public Path findShortestPath(Station sourceStation, Station targetStation) {
        validateStation(sourceStation, targetStation);
        try {
            return new Path(dijkstraShortestPath.getPath(sourceStation, targetStation));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }

    private void validateStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역은 같을 수 없습니다.");
        }
    }

}
