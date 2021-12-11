package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

public class PathFinder {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.dijkstraShortestPath = new DijkstraShortestPath<Station, DefaultWeightedEdge>(graph);
    }

    public static PathFinder of(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        lines.forEach(line -> {
            addVertex(graph, line);
            setEdgeWeight(graph, line.getSections());
        });
        return new PathFinder(graph);
    }
    
    public List<Station> findShortestPath(Station sourceStation, Station targetStation) {
        validationSameStation(sourceStation, targetStation);
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        return graphPath.getVertexList();
    }

    public int findShortestDistance(Station sourceStation, Station targetStation) {
        validationSameStation(sourceStation, targetStation);
        return (int) dijkstraShortestPath.getPathWeight(sourceStation, targetStation);
    }

    private static void addVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        line.getStations().forEach(station -> graph.addVertex(station));
    }

    private static void setEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Sections sections) {
        sections.getSections().forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().getDistance()));
    }
    
    private void validationSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다");
        }
    }

}
