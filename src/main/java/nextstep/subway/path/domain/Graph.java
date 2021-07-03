package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.SectionDistance;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Graph {
    private WeightedMultigraph<Station, SectionDistance> graph;

    public Graph() {
        graph = new WeightedMultigraph(SectionDistance.class);
    }

    public void build(List<Line> lines) {
        lines.forEach(line -> {
                    addVertexes(line);
                    setEdgeWeights(line);
                });
    }

    public Path findShortestPath(List<Line> lines, Station source, Station target) {
        build(lines);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Station, SectionDistance> graphPath = dijkstraShortestPath.getPath(source, target);
        verifyAvailable(graphPath);

        List<Station> stations = graphPath.getVertexList();
        ShortestDistance distance = new ShortestDistance((int) dijkstraShortestPath.getPathWeight(source, target));
        List<SectionDistance> sectionDistances = getSectionDistances(stations);

        return new Path(stations, distance, sectionDistances);
    }

    private List<SectionDistance> getSectionDistances(List<Station> stations) {
        List<SectionDistance> sectionDistances = new ArrayList<>();
        for (int i = 1; i < stations.size(); i++) {
            sectionDistances.add(graph.getEdge(stations.get(i-1), stations.get(i)));
        }
        return sectionDistances;
    }

    public List<Line> findLinesOf(Path path) {
        return path.getSectionDistances().stream()
                .map(SectionDistance::getLine)
                .collect(Collectors.toList());
    }

    public Set<Station> getVertexes() {
        return graph.vertexSet();
    }

    private void setEdgeWeights(Line line) {
        line.getSections()
                .forEach(section -> {
                    section.getDistance().setLine(line);
                    graph.addEdge(section.getUpStation(), section.getDownStation(), section.getDistance());
                    graph.setEdgeWeight(section.getDistance(), section.getWeight());
                });
    }

    private void addVertexes(Line line) {
        line.findStationsOrderUpToDown()
                .forEach(station -> graph.addVertex(station));
    }

    private void verifyAvailable(GraphPath<Station, SectionDistance> path) {
        if (Objects.isNull(path)) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }
    }

    public WeightedMultigraph<Station, SectionDistance> getGraph() {
        return graph;
    }
}
