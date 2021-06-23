package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
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

    public Set<Station> stationSet() {
        return graph.vertexSet();
    }

    public int findShortestPathDistance(List<Line> lines, Station source, Station target) {
        DijkstraShortestPath shortestPath = getDijkstraShortestPath(lines);
        getPath(source, target, shortestPath);
        return (int)shortestPath.getPathWeight(source, target);
    }

    public List<Station> findShortestPathStations(List<Line> lines, Station source, Station target) {
        DijkstraShortestPath shortestPath = getDijkstraShortestPath(lines);
        GraphPath path = getPath(source, target, shortestPath);
        return path.getVertexList();
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

    private GraphPath getPath(Station source, Station target, DijkstraShortestPath shortestPath) {
        GraphPath path = shortestPath.getPath(source, target);
        verifyAvailable(path);
        return path;
    }

    private DijkstraShortestPath getDijkstraShortestPath(List<Line> lines) {
        build(lines);
        return new DijkstraShortestPath(graph);
    }

    private void verifyAvailable(GraphPath path) {
        if (Objects.isNull(path)) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }
    }
}
