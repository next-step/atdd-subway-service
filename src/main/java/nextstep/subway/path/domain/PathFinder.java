package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Collections;
import java.util.List;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private PathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static PathFinder of(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        for (Line line : lines) {
            addVertex(graph, line);
            setEdgeWeight(graph, line);
        }
        return new PathFinder(graph);
    }

    private static void setEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        line.getSections().stream().forEach(
                section -> {
                    graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
                }
        );
    }

    private static void addVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        line.getSortedStations().stream().forEach(
                station -> graph.addVertex(station)
        );
    }

    public Path findPathBetweenStations(Station sourceStation, Station targetStation) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<Station> stationsPath = dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
        Long distance = (long)dijkstraShortestPath.getPath(sourceStation, targetStation).getWeight();
        return new Path(stationsPath, distance);
    }

}
