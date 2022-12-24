package nextstep.subway.path.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionEdge;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class PathFinder {

    public PathResult findPath(Station source, Station target, List<Section> sections){
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph(SectionEdge.class);
        DijkstraShortestPath stationGraph = getStationGraph(graph, sections);
        GraphPath path = stationGraph.getPath(source, target);

        return new PathResult(path.getVertexList(), (int)path.getWeight(), getContainLines(path));
    }

    private Set<Line> getContainLines(GraphPath path) {
        List<SectionEdge> edgeList = path.getEdgeList();
        Set<Line> lines = new HashSet<>();
        for (SectionEdge edge : edgeList) {
            lines.add(edge.getLine());
        }
        return lines;
    }

    private DijkstraShortestPath getStationGraph(WeightedMultigraph<Station, SectionEdge> graph, List<Section> sections) {
        setVertex(graph, sections);
        setEdgeWeight(graph, sections);
        return new DijkstraShortestPath(graph);
    }

    private void setVertex(WeightedMultigraph<Station, SectionEdge> graph, List<Section> sections) {
        sections.stream().forEach(
            section -> addVertex(graph, section)
        );
    }

    private void addVertex(WeightedMultigraph<Station, SectionEdge> graph, Section section){
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
    }

    private void setEdgeWeight(WeightedMultigraph<Station, SectionEdge> graph, List<Section> sections) {
        sections.stream().forEach(
            section -> addEdgeWeight(graph, section)
        );
    }

    private void addEdgeWeight(WeightedMultigraph<Station, SectionEdge> graph, Section section) {
        SectionEdge sectionEdge = new SectionEdge(section);
        graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        graph.setEdgeWeight(sectionEdge, section.getDistance());
    }

    private List<StationResponse> createStations(GraphPath path) {
        List<Station> vertexList = path.getVertexList();
        return vertexList.stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }
}
