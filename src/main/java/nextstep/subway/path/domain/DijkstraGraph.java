package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class DijkstraGraph implements Graph {

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public DijkstraGraph() {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
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

        init(lines);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(this.graph);
        GraphPath path = dijkstraShortestPath.getPath(source, target);

        if(Objects.isNull(path)) {
            throw new IllegalStateException("출발역과 도착역이 연결되지 않았습니다.");
        }

        List<Station> shortest = path.getVertexList();
        double weight = path.getWeight();

        return new PathResult(shortest, weight);
    }
}
