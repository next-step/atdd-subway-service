package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Component
public class JgraphtPathFinder implements PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public JgraphtPathFinder() {
        this.graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    }

    private void init(List<Line> lines) {

        List<Station> stations = lines.stream()
                .flatMap(l -> l.getStationsByOrder().stream())
                .collect(toList());

        List<Section> sections = lines.stream()
                .flatMap(l -> l.getSections().stream())
                .collect(toList());

        stations.forEach(s -> graph.addVertex(s));
        sections.forEach(s -> graph.setEdgeWeight(graph.addEdge(s.getUpStation(), s.getDownStation()), s.getDistance().getValue()));
    }

    @Override
    public PathResult find(List<Line> lines, Station source, Station target) {

        if(source == target) {
            throw new IllegalStateException("출발역과 도착역이 같습니다.");
        }
        init(lines);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(this.graph);
        GraphPath path = dijkstraShortestPath.getPath(source, target);

        if(Objects.isNull(path)) {
            throw new IllegalStateException("출발역과 도착역이 연결되지 않았습니다.");
        }
        return new PathResult(path.getVertexList(), path.getWeight());

    }
}
