package nextstep.subway.path.utils;

import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

public class Route {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    public PathResponse getShortestRoute(final Station startStation, final Station endStation) {
        makeEdge(endStation);
        final GraphPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph)
                .getPath(endStation, startStation);
        return new PathResponse(
                path.getVertexList().stream()
                        .map(StationResponse::of)
                        .collect(Collectors.toList()),
                Double.valueOf(path.getWeight()).intValue());
    }

    private void makeEdge(final Station endStation) {
        final Queue<Station> queue = new LinkedList<>();
        queue.add(endStation);

        while (!queue.isEmpty()) {
            final Station station = queue.poll();
            station.getSections().stream()
                    .peek(this::setVertexAndEdgeWeight)
                    .map(Section::getUpStation)
                    .forEach(queue::add);
        }
    }

    private void setVertexAndEdgeWeight(final Section section) {
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }
}
