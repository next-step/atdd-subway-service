package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionDistance;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Path {
    private WeightedMultigraph<Station, SectionDistance> graph;

    public Path() {
        graph = new WeightedMultigraph(SectionDistance.class);
    }

    public void build(List<Line> lines) {
        lines.stream()
                .forEach(line -> {
                    addStations(line);
                    setSectionDistances(line);
                });
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

    public Set<Station> stationSet() {
        return graph.vertexSet();
    }

    public List<Station> findShortestPathStations(List<Line> lines, Station source, Station target) {
        build(lines);
        DijkstraShortestPath shortestPath = new DijkstraShortestPath(graph);
        GraphPath path = shortestPath.getPath(source, target);
        if (Objects.isNull(path)) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }
        return path.getVertexList();
    }

    public int findShortestPathDistance(List<Line> lines, Station source, Station target) {
        build(lines);
        DijkstraShortestPath shortestPath = new DijkstraShortestPath(graph);
        GraphPath path = shortestPath.getPath(source, target);
        if (Objects.isNull(path)) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }
        return (int)shortestPath.getPathWeight(source, target);
    }
}
