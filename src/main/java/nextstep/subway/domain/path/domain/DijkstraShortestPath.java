package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Distance;
import nextstep.subway.domain.line.domain.Line;
import nextstep.subway.domain.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DijkstraShortestPath implements Path {

    private final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final org.jgrapht.alg.shortestpath.DijkstraShortestPath<Long, Long> dijkstraShortestPath = new org.jgrapht.alg.shortestpath.DijkstraShortestPath(graph);

    @Override
    public Distance getWeight(final Station source, final Station target) {
        GraphPath<Long, Long> path = dijkstraShortestPath.getPath(source.getId(), target.getId());
        if (path == null) {
            // 경로가 존재하지 않을 경우 발생시키고자 하는 예외는 NotConnectedStation 이다.
            // 경로가 없을 경우 Distance를 0으로 해서 넘기는게 맞으나 그렇게 되면 SectionDistanceLessThanMinimumException이 발생한다.
            // 위와 같은 이유로 NotConnectedStation을 발생시키기 위해 Distance에 임의의 값을 넣었으나, 주석을 남기지 않는 이상 다른 사람은 해당 값의 의미를 추측해야 하기 때문에 좋은 코드가 아니다.
            // 무엇이 좋은 방법일까?
            return new Distance(100);
        }
        return new Distance((int) path.getWeight());
    }

    @Override
    public void createVertex(final List<Line> lines) {
        lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(station -> graph.addVertex(station.getId()));
    }

    @Override
    public void createEdge(final List<Line> lines) {
        lines.stream()
                .map(Line::getSections)
                .flatMap(Collection::stream)
                .forEach(section ->
                        graph.setEdgeWeight(graph.addEdge(section.getUpStation().getId(), section.getDownStation().getId()), section.getDistance().getDistance()));
    }

    @Override
    public List<Station> getVertex(final List<Station> stations, final Station source, final Station target) {
        final GraphPath<Long, Long> path = dijkstraShortestPath.getPath(source.getId(), target.getId());

        try {
            return path.getVertexList().stream()
                    .map(stationId -> PathFinder.getStation(stations, stationId))
                    .collect(Collectors.toList());
        }catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }
}
