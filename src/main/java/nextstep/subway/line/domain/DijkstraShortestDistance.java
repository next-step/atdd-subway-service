package nextstep.subway.line.domain;

import nextstep.subway.exception.LineHasNotExistShortestException;
import nextstep.subway.exception.StationNotExistException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class DijkstraShortestDistance implements ShortestDistance {
    private final List<Line> lines;
    private final Station source;
    private final Station target;

    public DijkstraShortestDistance(List<Line> lines, Station source, Station target) {
        this.lines = lines;
        this.source = source;
        this.target = target;
    }

    @Override
    public Distance shortestDistance() {
        validateShortestRoute();

        return new Distance(getShortestGraph(getShortestLine()).getWeight());
    }

    @Override
    public Stations shortestRoute() {
        validateShortestRoute();

        return new Stations(getShortestGraph(getShortestLine()).getVertexList());
    }

    private Line getShortestLine() {
        return lines.stream()
                .filter(item -> item.containsStationsExactly(source, target))
                .min((l1, l2) -> {
                    Distance l1Distance = getShortestDistance(l1);
                    Distance l2Distance = getShortestDistance(l2);

                    return l1Distance.compareTo(l2Distance);
                })
                .orElseThrow(() -> new LineHasNotExistShortestException("최단거리가 존재하지 않습니다."));
    }

    private Distance getShortestDistance(Line line) {
        GraphPath<Station, DefaultWeightedEdge> path = getShortestGraph(line);

        return new Distance(path.getWeight());
    }

    private GraphPath<Station, DefaultWeightedEdge> getShortestGraph(Line line) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        line.getSections()
                .toCollection()
                .forEach(item -> {
                    graph.addVertex(item.getUpStation());
                    graph.addVertex(item.getDownStation());
                    graph.setEdgeWeight(graph.addEdge(item.getUpStation(), item.getDownStation()), item.getDistance().toInt());
                });

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        return path;
    }

    private void validateShortestRoute() {
        boolean notContainsStations = lines.stream()
                .noneMatch(item -> item.containsStationsExactly(source, target));
        if (notContainsStations) {
            throw new LineHasNotExistShortestException("포함되지 않은 역이 있습니다.");
        }

        if (source == target) {
            throw new LineHasNotExistShortestException("같은 역끼리는 길을 찾을 수 없습니다.");
        }
    }
}
