package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Line;
import nextstep.subway.domain.path.exception.SameDepartureAndArrivalStationException;
import nextstep.subway.domain.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PathFinder {

    private final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Long, Long> dijkstraShortestPath = new DijkstraShortestPath(graph);

    protected PathFinder() {
    }

    public PathFinder(final List<Line> lines) {
        createGraph(lines);
    }

    private void createGraph(final List<Line> lines) {
        createVertex(lines);
        createEdge(lines);
    }

    private void createEdge(final List<Line> lines) {
        lines.stream()
                .map(Line::getSections)
                .flatMap(Collection::stream)
                .forEach(section ->
            graph.setEdgeWeight(graph.addEdge(section.getUpStation().getId(), section.getDownStation().getId()), section.getDistance().getDistance()));
    }

    private void createVertex(final List<Line> lines) {
        lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(station -> graph.addVertex(station.getId()));
    }

    public List<Station> findShortestRoute(List<Station> stations, Long source, Long target) {
        sameDepartureAndArrivalStationValidator(source, target);
        final List<Long> vertexList = dijkstraShortestPath.getPath(source, target).getVertexList();
        return getShortestStations(stations, vertexList);
    }

    private void sameDepartureAndArrivalStationValidator(final Long source, final Long target) {
        if (source.equals(target)) {
            throw new SameDepartureAndArrivalStationException(String.format("departure : %d, arrival : %d", source, target));
        }
    }

    private List<Station> getShortestStations(final List<Station> stations, final List<Long> vertexList) {
        final List<Station> result = new ArrayList<>();
        for (int i = 0; i< vertexList.size(); i++) {
            int finalIndex = i;
            Station station = stations.stream()
                    .filter(st -> st.getId().equals(vertexList.get(finalIndex)))
                    .findFirst()
                    .get();
            result.add(station);
        }
        return result;
    }

    public int findShortestDistance(Long source, Long target) {
        return (int) dijkstraShortestPath.getPath(source, target).getWeight();
    }
}
