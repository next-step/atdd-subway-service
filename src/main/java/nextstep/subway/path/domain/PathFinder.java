package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;


public class PathFinder {
    private DijkstraShortestPath dijkstraShortestPath;

    private PathFinder(DijkstraShortestPath dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static PathFinder initialPathFinder(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        lines.forEach(line -> {
            line.getStations().forEach(station -> graph.addVertex(station));
            line.getSections().getSections().forEach(section ->
                    graph.setEdgeWeight(
                            graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()
                    )
            );
        });
        return new PathFinder(new DijkstraShortestPath(graph));
    }

    public PathResponse getShortestPath(Station source, Station target) {
        List<Station> stations = dijkstraShortestPath.getPath(source, target).getVertexList();
        Long totalDistance = (long) dijkstraShortestPath.getPath(source, target).getWeight();
        return PathResponse.of(stations, totalDistance);
    }

}
