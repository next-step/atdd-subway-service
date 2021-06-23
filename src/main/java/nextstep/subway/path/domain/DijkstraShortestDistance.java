package nextstep.subway.path.domain;

import nextstep.subway.exception.LineHasNotExistShortestException;
import nextstep.subway.exception.NoRouteException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.StationPair;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

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

        return new Distance(getShortestGraph().getWeight());
    }

    @Override
    public Stations shortestRoute() {
        validateShortestRoute();

        return new Stations(getShortestGraph().getVertexList());
    }

    @Override
    public Lines usedLines() {
        Lines lines = new Lines(this.lines);

        Stations stations = shortestRoute();
        List<StationPair> stationPair = stations.getSectionPair();

        return new Lines(stationPair.stream()
                .map(item -> lines.findCheapAndShortestBy(item))
                .collect(Collectors.toList()));
    }

    private GraphPath<Station, DefaultWeightedEdge> getShortestGraph() {

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        lines.stream()
                .flatMap(item -> item.getSections().toCollection().stream())
                .forEach(
                        item -> {
                            graph.addVertex(item.getUpStation());
                            graph.addVertex(item.getDownStation());
                            graph.setEdgeWeight(graph.addEdge(item.getUpStation(), item.getDownStation()), item.getDistance().toInt());
                        }
                );

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);

        if (path == null) {
            throw new NoRouteException("갈수 있는 길이 없습니다.");
        }

        return path;
    }

    private void validateShortestRoute() {
        if (source == target) {
            throw new LineHasNotExistShortestException("같은 역끼리는 길을 찾을 수 없습니다.");
        }
    }
}
