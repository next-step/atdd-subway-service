package nextstep.subway.path.infra;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.overfare.DefaultOverFare;
import nextstep.subway.path.domain.overfare.OverFare;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Component
public class JgraphtPathFinder implements PathFinder {

    public JgraphtPathFinder() {
    }

    private void validateLinkSourceAndTarget(GraphPath path) {
        if(Objects.isNull(path)) {
            throw new IllegalStateException("출발역과 도착역이 연결되지 않았습니다.");
        }
    }

    private void validateCorrectSourceAndTarget(Station source, Station target) {
        if(source == target) {
            throw new IllegalStateException("출발역과 도착역이 같습니다.");
        }
    }

    private DijkstraShortestPath createGraph(List<Line> lines) {

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        List<Station> stations = lines.stream()
                .flatMap(l -> l.getStationsByOrder().stream())
                .collect(toList());

        List<Section> sections = lines.stream()
                .flatMap(l -> l.getSections().stream())
                .collect(toList());

        stations.forEach(s -> graph.addVertex(s));
        sections.forEach(s -> graph.setEdgeWeight(graph.addEdge(s.getUpStation(), s.getDownStation()), s.getDistance().getValue()));
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath;
    }

    private GraphPath find(List<Line> lines, Station source, Station target) {
        DijkstraShortestPath graph = createGraph(lines);
        GraphPath path = graph.getPath(source, target);

        validateLinkSourceAndTarget(path);
        return path;
    }

    @Override
    public List<Station> findStations(List<Line> lines, Station source, Station target) {
        validateCorrectSourceAndTarget(source, target);
        GraphPath path = find(lines, source, target);
        return path.getVertexList();
    }

    @Override
    public Distance findDistance(List<Line> lines, Station source, Station target) {
        validateCorrectSourceAndTarget(source, target);
        GraphPath path = find(lines, source, target);
        return new Distance((int)path.getWeight());
    }

    @Override
    public ShortestPath findShortestPath(List<Line> lines, Station source, Station target) {
        validateCorrectSourceAndTarget(source, target);
        GraphPath path = find(lines, source, target);
        return new ShortestPath(path.getVertexList(), (int)path.getWeight());
    }

}
