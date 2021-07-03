package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {
    WeightedMultigraph<Station, SectionEdge> graph;
    DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath;

    public static PathFinder of(List<Station> stations, List<Section> sections) {
        PathFinder pathFinder = new PathFinder();
        pathFinder.init(stations, sections);
        return pathFinder;
    }

    public Path getPath(Station sourceStation, Station targetStation) {
        validateEqualStation(sourceStation, targetStation);

        List<Station> travelStations = getTravelStations(sourceStation, targetStation);
        double distance = getDistance(sourceStation, targetStation);
        List<Line> transferLine = getTransferLine(sourceStation, targetStation);
        return Path.of(travelStations, distance, transferLine);
    }

    private List<Line> getTransferLine(Station sourceStation, Station targetStation) {
        return dijkstraShortestPath.getPath(sourceStation, targetStation).getEdgeList().stream()
                .map(sectionEdge -> sectionEdge.getSection().getLine())
                .collect(Collectors.toList());
    }

    private double getDistance(Station sourceStation, Station targetStation) {
        return dijkstraShortestPath.getPath(sourceStation, targetStation).getWeight();
    }

    private List<Station> getTravelStations(Station sourceStation, Station targetStation) {
        return dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
    }

    private void validateEqualStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException();
        }
    }

    private void init(List<Station> stations, List<Section> sections) {
        graph = new WeightedMultigraph(SectionEdge.class);
        dijkstraShortestPath = new DijkstraShortestPath(graph);
        addVertex(stations);
        addEdge(sections);
    }

    private void addEdge(List<Section> sections) {
        sections.forEach(section ->
        {
            SectionEdge sectionEdge = SectionEdge.of(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
            graph.setEdgeWeight(sectionEdge, section.getDistance());
        });
    }

    private void addVertex(List<Station> stations) {
        stations.forEach(graph::addVertex);
    }
}
