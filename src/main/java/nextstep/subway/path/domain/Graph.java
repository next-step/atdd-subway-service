package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.SectionDistance;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class Graph {
    private WeightedMultigraph<Station, SectionDistance> graph;

    public Graph() {
        graph = new WeightedMultigraph(SectionDistance.class);
    }

    public void build(List<Line> lines) {
        lines.stream()
                .forEach(line -> {
                    addStations(line);
                    setSectionDistances(line);
                });
    }

    public Path findShortestPath(Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath graphPath = dijkstraShortestPath.getPath(source, target);
        verifyAvailable(graphPath);

        List<Station> stations = graphPath.getVertexList();
        ShortestDistance distance = new ShortestDistance((int) dijkstraShortestPath.getPathWeight(source, target));
        return new Path(stations, distance);
    }

    public Set<Station> stationSet() {
        return graph.vertexSet();
    }

    private void setSectionDistances(Line line) {
        line.getSections()
                .forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(),
                        section.getDownStation()), section.getDistance()));
    }

    private void addStations(Line line) {
        line.findStationsOrderUpToDown().stream()
                .forEach(station -> graph.addVertex(station));
    }

    private void verifyAvailable(GraphPath path) {
        if (Objects.isNull(path)) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }
    }
}
